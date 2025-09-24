#!/bin/bash

# AWS Fargate Deployment Script for Malayalees Application
# This script automates the complete deployment process

set -e  # Exit on any error

# Configuration
AWS_REGION="us-east-1"
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
APP_NAME="malayalees-app"
CLUSTER_NAME="malayalees-cluster"
SERVICE_NAME="malayalees-service"
DB_INSTANCE_ID="malayalees-postgres"
VPC_ID="vpc-12345678"  # Replace with your VPC ID
SUBNET_IDS="subnet-12345678,subnet-87654321"  # Replace with your subnet IDs
SECURITY_GROUP_ID="sg-12345678"  # Replace with your security group ID

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."

    # Check AWS CLI
    if ! command -v aws &> /dev/null; then
        log_error "AWS CLI is not installed. Please install it first."
        exit 1
    fi

    # Check Docker
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed. Please install it first."
        exit 1
    fi

    # Check Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven is not installed. Please install it first."
        exit 1
    fi

    # Check AWS credentials
    if ! aws sts get-caller-identity &> /dev/null; then
        log_error "AWS credentials not configured. Please run 'aws configure' first."
        exit 1
    fi

    log_success "All prerequisites met!"
}

# Create ECR repository
create_ecr_repository() {
    log_info "Creating ECR repository..."

    if aws ecr describe-repositories --repository-names $APP_NAME --region $AWS_REGION &> /dev/null; then
        log_warning "ECR repository $APP_NAME already exists"
    else
        aws ecr create-repository \
            --repository-name $APP_NAME \
            --region $AWS_REGION \
            --image-scanning-configuration scanOnPush=true
        log_success "ECR repository created"
    fi
}

# Build and push Docker image
build_and_push_image() {
    log_info "Building application..."

    # Build Maven project
    ./mvnw clean package -DskipTests

    log_info "Building Docker image..."

    # Login to ECR
    aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

    # Build image
    docker build -f src/main/docker/Docker_Fargate/Dockerfile.local -t $APP_NAME .

    # Tag image
    docker tag $APP_NAME:latest $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$APP_NAME:latest

    # Push image
    docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$APP_NAME:latest

    log_success "Docker image built and pushed to ECR"
}

# Create RDS parameter group
create_rds_parameter_group() {
    log_info "Creating RDS parameter group..."

    if aws rds describe-db-parameter-groups --db-parameter-group-name malayalees-postgres-cache --region $AWS_REGION &> /dev/null; then
        log_warning "RDS parameter group already exists"
    else
        aws rds create-db-parameter-group \
            --db-parameter-group-name malayalees-postgres-cache \
            --db-parameter-group-family postgres13 \
            --description "PostgreSQL parameter group optimized for caching" \
            --region $AWS_REGION

        # Set caching parameters
        aws rds modify-db-parameter-group \
            --db-parameter-group-name malayalees-postgres-cache \
            --parameters \
                ParameterName=shared_buffers,ParameterValue=2GB,ApplyMethod=immediate \
                ParameterName=effective_cache_size,ParameterValue=6GB,ApplyMethod=immediate \
                ParameterName=work_mem,ParameterValue=64MB,ApplyMethod=immediate \
                ParameterName=maintenance_work_mem,ParameterValue=512MB,ApplyMethod=immediate \
                ParameterName=shared_preload_libraries,ParameterValue=pg_stat_statements,ApplyMethod=immediate \
                ParameterName=max_connections,ParameterValue=200,ApplyMethod=immediate \
            --region $AWS_REGION

        log_success "RDS parameter group created"
    fi
}

# Create RDS subnet group
create_rds_subnet_group() {
    log_info "Creating RDS subnet group..."

    if aws rds describe-db-subnet-groups --db-subnet-group-name malayalees-subnet-group --region $AWS_REGION &> /dev/null; then
        log_warning "RDS subnet group already exists"
    else
        aws rds create-db-subnet-group \
            --db-subnet-group-name malayalees-subnet-group \
            --db-subnet-group-description "Subnet group for Malayalees RDS" \
            --subnet-ids $SUBNET_IDS \
            --region $AWS_REGION

        log_success "RDS subnet group created"
    fi
}

# Create RDS instance
create_rds_instance() {
    log_info "Creating RDS instance..."

    if aws rds describe-db-instances --db-instance-identifier $DB_INSTANCE_ID --region $AWS_REGION &> /dev/null; then
        log_warning "RDS instance $DB_INSTANCE_ID already exists"
    else
        aws rds create-db-instance \
            --db-instance-identifier $DB_INSTANCE_ID \
            --db-instance-class db.t3.micro \
            --engine postgres \
            --engine-version 13.7 \
            --master-username malayalees_admin \
            --master-user-password "YourSecurePassword123!" \
            --allocated-storage 20 \
            --storage-type gp2 \
            --vpc-security-group-ids $SECURITY_GROUP_ID \
            --db-subnet-group-name malayalees-subnet-group \
            --db-parameter-group-name malayalees-postgres-cache \
            --backup-retention-period 7 \
            --multi-az \
            --storage-encrypted \
            --deletion-protection \
            --region $AWS_REGION

        log_success "RDS instance created (this may take several minutes)"
        log_info "Waiting for RDS instance to be available..."

        aws rds wait db-instance-available --db-instance-identifier $DB_INSTANCE_ID --region $AWS_REGION
        log_success "RDS instance is available"
    fi
}

# Create AWS Secrets Manager secrets
create_secrets() {
    log_info "Creating AWS Secrets Manager secrets..."

    # Database password
    if ! aws secretsmanager describe-secret --secret-id malayalees/db-password --region $AWS_REGION &> /dev/null; then
        aws secretsmanager create-secret \
            --name malayalees/db-password \
            --description "Database password for Malayalees application" \
            --secret-string "YourAppPassword123!" \
            --region $AWS_REGION
    fi

    # JWT secret
    if ! aws secretsmanager describe-secret --secret-id malayalees/jwt-secret --region $AWS_REGION &> /dev/null; then
        aws secretsmanager create-secret \
            --name malayalees/jwt-secret \
            --description "JWT secret for Malayalees application" \
            --secret-string "mySecretKey12345678901234567890123456789012345678901234567890" \
            --region $AWS_REGION
    fi

    # JWT base64 secret
    if ! aws secretsmanager describe-secret --secret-id malayalees/jwt-base64-secret --region $AWS_REGION &> /dev/null; then
        aws secretsmanager create-secret \
            --name malayalees/jwt-base64-secret \
            --description "JWT base64 secret for Malayalees application" \
            --secret-string "bXlTZWNyZXRLZXkxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA==" \
            --region $AWS_REGION
    fi

    # AWS credentials
    if ! aws secretsmanager describe-secret --secret-id malayalees/aws-access-key --region $AWS_REGION &> /dev/null; then
        aws secretsmanager create-secret \
            --name malayalees/aws-access-key \
            --description "AWS access key for Malayalees application" \
            --secret-string "AKIATIT5HARDKCWNLQMU" \
            --region $AWS_REGION
    fi

    if ! aws secretsmanager describe-secret --secret-id malayalees/aws-secret-key --region $AWS_REGION &> /dev/null; then
        aws secretsmanager create-secret \
            --name malayalees/aws-secret-key \
            --description "AWS secret key for Malayalees application" \
            --secret-string "9xyoyfvKjMJzRhDBZEkqM/qatrGUtV4IVO6CuIBo" \
            --region $AWS_REGION
    fi

    # Twilio credentials
    if ! aws secretsmanager describe-secret --secret-id malayalees/twilio-account-sid --region $AWS_REGION &> /dev/null; then
        aws secretsmanager create-secret \
            --name malayalees/twilio-account-sid \
            --description "Twilio account SID for Malayalees application" \
            --secret-string "AC48380299acc5e7e27aee75a3108c3058" \
            --region $AWS_REGION
    fi

    if ! aws secretsmanager describe-secret --secret-id malayalees/twilio-auth-token --region $AWS_REGION &> /dev/null; then
        aws secretsmanager create-secret \
            --name malayalees/twilio-auth-token \
            --description "Twilio auth token for Malayalees application" \
            --secret-string "f460b6085002a38cf3c5cadd61c21057" \
            --region $AWS_REGION
    fi

    log_success "All secrets created"
}

# Create IAM roles
create_iam_roles() {
    log_info "Creating IAM roles..."

    # Create ECS task execution role
    if ! aws iam get-role --role-name ecsTaskExecutionRole &> /dev/null; then
        aws iam create-role \
            --role-name ecsTaskExecutionRole \
            --assume-role-policy-document '{
                "Version": "2012-10-17",
                "Statement": [
                    {
                        "Effect": "Allow",
                        "Principal": {
                            "Service": "ecs-tasks.amazonaws.com"
                        },
                        "Action": "sts:AssumeRole"
                    }
                ]
            }'

        aws iam attach-role-policy \
            --role-name ecsTaskExecutionRole \
            --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy

        log_success "ECS task execution role created"
    else
        log_warning "ECS task execution role already exists"
    fi

    # Create ECS task role
    if ! aws iam get-role --role-name ecsTaskRole &> /dev/null; then
        aws iam create-role \
            --role-name ecsTaskRole \
            --assume-role-policy-document '{
                "Version": "2012-10-17",
                "Statement": [
                    {
                        "Effect": "Allow",
                        "Principal": {
                            "Service": "ecs-tasks.amazonaws.com"
                        },
                        "Action": "sts:AssumeRole"
                    }
                ]
            }'

        log_success "ECS task role created"
    else
        log_warning "ECS task role already exists"
    fi
}

# Create ECS cluster
create_ecs_cluster() {
    log_info "Creating ECS cluster..."

    if aws ecs describe-clusters --clusters $CLUSTER_NAME --region $AWS_REGION &> /dev/null; then
        log_warning "ECS cluster $CLUSTER_NAME already exists"
    else
        aws ecs create-cluster \
            --cluster-name $CLUSTER_NAME \
            --capacity-providers FARGATE \
            --default-capacity-provider-strategy capacityProvider=FARGATE,weight=1 \
            --region $AWS_REGION

        log_success "ECS cluster created"
    fi
}

# Create CloudWatch log group
create_log_group() {
    log_info "Creating CloudWatch log group..."

    if aws logs describe-log-groups --log-group-name-prefix /ecs/$APP_NAME --region $AWS_REGION | grep -q "/ecs/$APP_NAME"; then
        log_warning "CloudWatch log group already exists"
    else
        aws logs create-log-group \
            --log-group-name /ecs/$APP_NAME \
            --retention-in-days 30 \
            --region $AWS_REGION

        log_success "CloudWatch log group created"
    fi
}

# Create task definition
create_task_definition() {
    log_info "Creating task definition..."

    # Get RDS endpoint
    RDS_ENDPOINT=$(aws rds describe-db-instances --db-instance-identifier $DB_INSTANCE_ID --region $AWS_REGION --query 'DBInstances[0].Endpoint.Address' --output text)

    # Create task definition JSON
    cat > task-definition.json << EOF
{
  "family": "$APP_NAME",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::$AWS_ACCOUNT_ID:role/ecsTaskExecutionRole",
  "taskRoleArn": "arn:aws:iam::$AWS_ACCOUNT_ID:role/ecsTaskRole",
  "containerDefinitions": [
    {
      "name": "$APP_NAME",
      "image": "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$APP_NAME:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "prod-aws-postgres-cache"
        },
        {
          "name": "RDS_ENDPOINT",
          "value": "$RDS_ENDPOINT"
        },
        {
          "name": "DB_NAME",
          "value": "malayalees_us_site"
        },
        {
          "name": "DB_USERNAME",
          "value": "malayalees_app"
        },
        {
          "name": "CACHE_TYPE",
          "value": "postgresql"
        },
        {
          "name": "POSTGRES_CACHE_TABLE",
          "value": "app_cache"
        }
      ],
      "secrets": [
        {
          "name": "DB_PASSWORD",
          "valueFrom": "arn:aws:secretsmanager:$AWS_REGION:$AWS_ACCOUNT_ID:secret:malayalees/db-password"
        },
        {
          "name": "JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET",
          "valueFrom": "arn:aws:secretsmanager:$AWS_REGION:$AWS_ACCOUNT_ID:secret:malayalees/jwt-secret"
        },
        {
          "name": "JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET",
          "valueFrom": "arn:aws:secretsmanager:$AWS_REGION:$AWS_ACCOUNT_ID:secret:malayalees/jwt-base64-secret"
        },
        {
          "name": "AWS_ACCESS_KEY_ID",
          "valueFrom": "arn:aws:secretsmanager:$AWS_REGION:$AWS_ACCOUNT_ID:secret:malayalees/aws-access-key"
        },
        {
          "name": "AWS_SECRET_ACCESS_KEY",
          "valueFrom": "arn:aws:secretsmanager:$AWS_REGION:$AWS_ACCOUNT_ID:secret:malayalees/aws-secret-key"
        },
        {
          "name": "TWILIO_ACCOUNT_SID",
          "valueFrom": "arn:aws:secretsmanager:$AWS_REGION:$AWS_ACCOUNT_ID:secret:malayalees/twilio-account-sid"
        },
        {
          "name": "TWILIO_AUTH_TOKEN",
          "valueFrom": "arn:aws:secretsmanager:$AWS_REGION:$AWS_ACCOUNT_ID:secret:malayalees/twilio-auth-token"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/$APP_NAME",
          "awslogs-region": "$AWS_REGION",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "healthCheck": {
        "command": [
          "CMD-SHELL",
          "curl -f http://localhost:8080/management/health || exit 1"
        ],
        "interval": 30,
        "timeout": 5,
        "retries": 3,
        "startPeriod": 60
      }
    }
  ]
}
EOF

    # Register task definition
    aws ecs register-task-definition --cli-input-json file://task-definition.json --region $AWS_REGION

    log_success "Task definition created"
}

# Create load balancer
create_load_balancer() {
    log_info "Creating Application Load Balancer..."

    # Create target group
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name malayalees-targets \
        --protocol HTTP \
        --port 8080 \
        --vpc-id $VPC_ID \
        --target-type ip \
        --health-check-path /management/health \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region $AWS_REGION \
        --query 'TargetGroups[0].TargetGroupArn' --output text)

    # Create load balancer
    ALB_ARN=$(aws elbv2 create-load-balancer \
        --name malayalees-alb \
        --subnets $SUBNET_IDS \
        --security-groups $SECURITY_GROUP_ID \
        --scheme internet-facing \
        --type application \
        --ip-address-type ipv4 \
        --region $AWS_REGION \
        --query 'LoadBalancers[0].LoadBalancerArn' --output text)

    # Create listener
    aws elbv2 create-listener \
        --load-balancer-arn $ALB_ARN \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn=$TARGET_GROUP_ARN \
        --region $AWS_REGION

    log_success "Load balancer created"
    log_info "Load balancer DNS name: $(aws elbv2 describe-load-balancers --load-balancer-arns $ALB_ARN --region $AWS_REGION --query 'LoadBalancers[0].DNSName' --output text)"
}

# Create ECS service
create_ecs_service() {
    log_info "Creating ECS service..."

    if aws ecs describe-services --cluster $CLUSTER_NAME --services $SERVICE_NAME --region $AWS_REGION &> /dev/null; then
        log_warning "ECS service $SERVICE_NAME already exists"
    else
        # Get target group ARN
        TARGET_GROUP_ARN=$(aws elbv2 describe-target-groups --names malayalees-targets --region $AWS_REGION --query 'TargetGroups[0].TargetGroupArn' --output text)

        aws ecs create-service \
            --cluster $CLUSTER_NAME \
            --service-name $SERVICE_NAME \
            --task-definition $APP_NAME:1 \
            --desired-count 2 \
            --launch-type FARGATE \
            --platform-version LATEST \
            --network-configuration "awsvpcConfiguration={subnets=[${SUBNET_IDS//,/,}],securityGroups=[$SECURITY_GROUP_ID],assignPublicIp=ENABLED}" \
            --load-balancers "targetGroupArn=$TARGET_GROUP_ARN,containerName=$APP_NAME,containerPort=8080" \
            --enable-execute-command \
            --enable-logging \
            --region $AWS_REGION

        log_success "ECS service created"
    fi
}

# Setup database
setup_database() {
    log_info "Setting up database..."

    # Get RDS endpoint
    RDS_ENDPOINT=$(aws rds describe-db-instances --db-instance-identifier $DB_INSTANCE_ID --region $AWS_REGION --query 'DBInstances[0].Endpoint.Address' --output text)

    log_info "RDS endpoint: $RDS_ENDPOINT"
    log_info "Please run the following commands to setup the database:"
    echo ""
    echo "1. Connect to RDS:"
    echo "   psql -h $RDS_ENDPOINT -U malayalees_admin -d postgres"
    echo ""
    echo "2. Create application database:"
    echo "   CREATE DATABASE malayalees_us_site;"
    echo ""
    echo "3. Create application user:"
    echo "   CREATE USER malayalees_app WITH PASSWORD 'YourAppPassword123!';"
    echo ""
    echo "4. Grant permissions:"
    echo "   GRANT ALL PRIVILEGES ON DATABASE malayalees_us_site TO malayalees_app;"
    echo "   \\c malayalees_us_site"
    echo "   GRANT ALL PRIVILEGES ON SCHEMA public TO malayalees_app;"
    echo "   GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO malayalees_app;"
    echo "   GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO malayalees_app;"
    echo ""
    echo "5. Run database migrations:"
    echo "   java -jar target/nextjs-template-boot-0.0.1-SNAPSHOT.jar \\"
    echo "     --spring.profiles.active=prod-aws-postgres-cache \\"
    echo "     --spring.datasource.url=jdbc:postgresql://$RDS_ENDPOINT:5432/malayalees_us_site \\"
    echo "     --spring.datasource.username=malayalees_app \\"
    echo "     --spring.datasource.password=YourAppPassword123! \\"
    echo "     --spring.liquibase.enabled=true"
}

# Main deployment function
deploy() {
    log_info "Starting AWS Fargate deployment for Malayalees application..."

    check_prerequisites
    create_ecr_repository
    build_and_push_image
    create_rds_parameter_group
    create_rds_subnet_group
    create_rds_instance
    create_secrets
    create_iam_roles
    create_ecs_cluster
    create_log_group
    create_task_definition
    create_load_balancer
    create_ecs_service
    setup_database

    log_success "Deployment completed successfully!"
    log_info "Next steps:"
    echo "1. Setup the database using the commands shown above"
    echo "2. Wait for the ECS service to be stable"
    echo "3. Test the application via the load balancer URL"
    echo "4. Configure monitoring and alerts"
}

# Cleanup function
cleanup() {
    log_info "Cleaning up AWS resources..."

    # Delete ECS service
    aws ecs update-service --cluster $CLUSTER_NAME --service $SERVICE_NAME --desired-count 0 --region $AWS_REGION
    aws ecs wait services-stable --cluster $CLUSTER_NAME --services $SERVICE_NAME --region $AWS_REGION
    aws ecs delete-service --cluster $CLUSTER_NAME --service $SERVICE_NAME --region $AWS_REGION

    # Delete load balancer
    ALB_ARN=$(aws elbv2 describe-load-balancers --names malayalees-alb --region $AWS_REGION --query 'LoadBalancers[0].LoadBalancerArn' --output text)
    aws elbv2 delete-load-balancer --load-balancer-arn $ALB_ARN --region $AWS_REGION

    # Delete target group
    TARGET_GROUP_ARN=$(aws elbv2 describe-target-groups --names malayalees-targets --region $AWS_REGION --query 'TargetGroups[0].TargetGroupArn' --output text)
    aws elbv2 delete-target-group --target-group-arn $TARGET_GROUP_ARN --region $AWS_REGION

    # Delete RDS instance
    aws rds delete-db-instance --db-instance-identifier $DB_INSTANCE_ID --skip-final-snapshot --region $AWS_REGION

    # Delete ECS cluster
    aws ecs delete-cluster --cluster $CLUSTER_NAME --region $AWS_REGION

    # Delete ECR repository
    aws ecr delete-repository --repository-name $APP_NAME --force --region $AWS_REGION

    # Delete secrets
    aws secretsmanager delete-secret --secret-id malayalees/db-password --force-delete-without-recovery --region $AWS_REGION
    aws secretsmanager delete-secret --secret-id malayalees/jwt-secret --force-delete-without-recovery --region $AWS_REGION
    aws secretsmanager delete-secret --secret-id malayalees/jwt-base64-secret --force-delete-without-recovery --region $AWS_REGION
    aws secretsmanager delete-secret --secret-id malayalees/aws-access-key --force-delete-without-recovery --region $AWS_REGION
    aws secretsmanager delete-secret --secret-id malayalees/aws-secret-key --force-delete-without-recovery --region $AWS_REGION
    aws secretsmanager delete-secret --secret-id malayalees/twilio-account-sid --force-delete-without-recovery --region $AWS_REGION
    aws secretsmanager delete-secret --secret-id malayalees/twilio-auth-token --force-delete-without-recovery --region $AWS_REGION

    # Delete log group
    aws logs delete-log-group --log-group-name /ecs/$APP_NAME --region $AWS_REGION

    log_success "Cleanup completed"
}

# Show usage
usage() {
    echo "Usage: $0 [deploy|cleanup]"
    echo ""
    echo "Commands:"
    echo "  deploy   - Deploy the application to AWS Fargate"
    echo "  cleanup  - Remove all AWS resources"
    echo ""
    echo "Before running, make sure to:"
    echo "1. Configure AWS CLI with appropriate credentials"
    echo "2. Update the configuration variables in this script"
    echo "3. Ensure you have the necessary AWS permissions"
}

# Main script logic
case "${1:-}" in
    deploy)
        deploy
        ;;
    cleanup)
        cleanup
        ;;
    *)
        usage
        exit 1
        ;;
esac
