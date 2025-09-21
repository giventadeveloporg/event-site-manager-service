#!/bin/bash

# ===================================================================
# AWS Deployment Script for Multi-Tenant Spring Boot Application
# ===================================================================
# This script automates the deployment of the Spring Boot application
# to AWS with proper configuration and monitoring setup.
# ===================================================================

set -e  # Exit on any error

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
REGION="us-east-1"
APPLICATION_NAME="malayalees-us-site-boot"
ENVIRONMENT="prod"
VERSION=${1:-"latest"}

# AWS Resources
ECR_REPOSITORY="123456789012.dkr.ecr.us-east-1.amazonaws.com/${APPLICATION_NAME}"
ECS_CLUSTER="${APPLICATION_NAME}-cluster"
ECS_SERVICE="${APPLICATION_NAME}-service"
TASK_DEFINITION="${APPLICATION_NAME}-task"
ALB_TARGET_GROUP="${APPLICATION_NAME}-tg"

# Environment Variables
export AWS_DEFAULT_REGION=${REGION}

# Logging function
log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
    exit 1
}

# Check prerequisites
check_prerequisites() {
    log "Checking prerequisites..."
    
    # Check AWS CLI
    if ! command -v aws &> /dev/null; then
        error "AWS CLI is not installed. Please install it first."
    fi
    
    # Check Docker
    if ! command -v docker &> /dev/null; then
        error "Docker is not installed. Please install it first."
    fi
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        error "Maven is not installed. Please install it first."
    fi
    
    # Check AWS credentials
    if ! aws sts get-caller-identity &> /dev/null; then
        error "AWS credentials not configured. Please run 'aws configure' first."
    fi
    
    success "Prerequisites check passed"
}

# Build application
build_application() {
    log "Building Spring Boot application..."
    
    # Clean and build
    ./mvnw clean package -Pprod-aws -DskipTests -q
    
    if [ ! -f "target/${APPLICATION_NAME}-${VERSION}.jar" ]; then
        error "Build failed. JAR file not found."
    fi
    
    success "Application built successfully"
}

# Build and push Docker image
build_and_push_image() {
    log "Building and pushing Docker image..."
    
    # Login to ECR
    aws ecr get-login-password --region ${REGION} | docker login --username AWS --password-stdin ${ECR_REPOSITORY}
    
    # Build Docker image
    docker build -t ${APPLICATION_NAME}:${VERSION} .
    docker tag ${APPLICATION_NAME}:${VERSION} ${ECR_REPOSITORY}:${VERSION}
    docker tag ${APPLICATION_NAME}:${VERSION} ${ECR_REPOSITORY}:latest
    
    # Push to ECR
    docker push ${ECR_REPOSITORY}:${VERSION}
    docker push ${ECR_REPOSITORY}:latest
    
    success "Docker image pushed successfully"
}

# Deploy infrastructure using CDK
deploy_infrastructure() {
    log "Deploying infrastructure with AWS CDK..."
    
    if [ ! -d "infrastructure" ]; then
        warning "Infrastructure directory not found. Skipping CDK deployment."
        return
    fi
    
    cd infrastructure
    
    # Install dependencies
    npm install
    
    # Deploy stack
    npx cdk deploy --require-approval never
    
    cd ..
    
    success "Infrastructure deployed successfully"
}

# Update ECS service
update_ecs_service() {
    log "Updating ECS service..."
    
    # Update task definition with new image
    TASK_DEFINITION_ARN=$(aws ecs describe-task-definition \
        --task-definition ${TASK_DEFINITION} \
        --query 'taskDefinition.taskDefinitionArn' \
        --output text)
    
    # Create new task definition revision
    aws ecs register-task-definition \
        --cli-input-json file://task-definition.json \
        --query 'taskDefinition.taskDefinitionArn' \
        --output text
    
    # Update ECS service
    aws ecs update-service \
        --cluster ${ECS_CLUSTER} \
        --service ${ECS_SERVICE} \
        --task-definition ${TASK_DEFINITION} \
        --force-new-deployment
    
    success "ECS service updated successfully"
}

# Run database migrations
run_migrations() {
    log "Running database migrations..."
    
    # Get RDS endpoint from Parameter Store
    RDS_ENDPOINT=$(aws ssm get-parameter \
        --name "/${APPLICATION_NAME}/${ENVIRONMENT}/database/endpoint" \
        --query 'Parameter.Value' \
        --output text)
    
    # Run migrations using a temporary ECS task
    aws ecs run-task \
        --cluster ${ECS_CLUSTER} \
        --task-definition ${TASK_DEFINITION} \
        --overrides '{
            "containerOverrides": [{
                "name": "'${APPLICATION_NAME}'",
                "command": ["java", "-jar", "app.jar", "--spring.profiles.active=prod-aws", "--spring.liquibase.enabled=true"]
            }]
        }' \
        --wait-for-completion
    
    success "Database migrations completed"
}

# Health check
health_check() {
    log "Performing health check..."
    
    # Get ALB DNS name
    ALB_DNS=$(aws elbv2 describe-load-balancers \
        --names "${APPLICATION_NAME}-alb" \
        --query 'LoadBalancers[0].DNSName' \
        --output text)
    
    # Wait for service to be healthy
    for i in {1..30}; do
        if curl -f -s "http://${ALB_DNS}/management/health" > /dev/null; then
            success "Health check passed"
            return 0
        fi
        log "Health check attempt $i/30 - waiting..."
        sleep 10
    done
    
    error "Health check failed after 5 minutes"
}

# Rollback function
rollback() {
    warning "Rolling back deployment..."
    
    # Get previous task definition
    PREVIOUS_TASK_DEFINITION=$(aws ecs describe-task-definition \
        --task-definition ${TASK_DEFINITION} \
        --query 'taskDefinition.revision' \
        --output text)
    
    PREVIOUS_REVISION=$((PREVIOUS_TASK_DEFINITION - 1))
    
    # Update service to previous revision
    aws ecs update-service \
        --cluster ${ECS_CLUSTER} \
        --service ${ECS_SERVICE} \
        --task-definition ${TASK_DEFINITION}:${PREVIOUS_REVISION} \
        --force-new-deployment
    
    success "Rollback completed"
}

# Main deployment function
main() {
    log "Starting deployment of ${APPLICATION_NAME} version ${VERSION}"
    
    # Trap errors for rollback
    trap 'error "Deployment failed. Check logs for details."' ERR
    
    check_prerequisites
    build_application
    build_and_push_image
    deploy_infrastructure
    update_ecs_service
    run_migrations
    health_check
    
    success "Deployment completed successfully!"
    log "Application is available at: http://$(aws elbv2 describe-load-balancers --names "${APPLICATION_NAME}-alb" --query 'LoadBalancers[0].DNSName' --output text)"
}

# Script entry point
case "${1:-deploy}" in
    "deploy")
        main
        ;;
    "rollback")
        rollback
        ;;
    "health")
        health_check
        ;;
    "build")
        check_prerequisites
        build_application
        build_and_push_image
        ;;
    *)
        echo "Usage: $0 {deploy|rollback|health|build} [version]"
        echo "  deploy  - Full deployment (default)"
        echo "  rollback - Rollback to previous version"
        echo "  health  - Check application health"
        echo "  build   - Build and push image only"
        exit 1
        ;;
esac
