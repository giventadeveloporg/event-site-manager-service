#!/bin/bash

# ===================================================================
# Local Testing Script for Multi-Tenant Spring Boot Application
# ===================================================================
# This script sets up and tests the complete local environment
# including PostgreSQL, Redis, and the Spring Boot application.
# ===================================================================

set -e  # Exit on any error

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
APPLICATION_NAME="malayalees-us-site-boot"
LOCAL_PORT=8080
POSTGRES_PORT=5432
REDIS_PORT=6379
PGADMIN_PORT=5050
REDIS_COMMANDER_PORT=8081
MAILHOG_PORT=8025

# Logging functions
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

info() {
    echo -e "${PURPLE}[INFO]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log "Checking prerequisites..."
    
    # Check Docker
    if ! command -v docker &> /dev/null; then
        error "Docker is not installed. Please install Docker first."
    fi
    
    # Check Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        error "Docker Compose is not installed. Please install Docker Compose first."
    fi
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        error "Maven is not installed. Please install Maven first."
    fi
    
    # Check Java
    if ! command -v java &> /dev/null; then
        error "Java is not installed. Please install Java 11+ first."
    fi
    
    # Check Docker daemon
    if ! docker info &> /dev/null; then
        error "Docker daemon is not running. Please start Docker first."
    fi
    
    success "Prerequisites check passed"
}

# Build application
build_application() {
    log "Building Spring Boot application..."
    
    # Clean and build
    ./mvnw clean package -DskipTests -q
    
    if [ ! -f "target/${APPLICATION_NAME}-*.jar" ]; then
        error "Build failed. JAR file not found."
    fi
    
    success "Application built successfully"
}

# Start infrastructure services
start_infrastructure() {
    log "Starting infrastructure services (PostgreSQL, Redis)..."
    
    # Start only infrastructure services first
    docker-compose -f docker-compose.local.yml up -d postgres redis
    
    # Wait for services to be healthy
    log "Waiting for PostgreSQL to be ready..."
    timeout 60 bash -c 'until docker-compose -f docker-compose.local.yml exec postgres pg_isready -U postgres -d malayalees_us_site; do sleep 2; done'
    
    log "Waiting for Redis to be ready..."
    timeout 60 bash -c 'until docker-compose -f docker-compose.local.yml exec redis redis-cli ping | grep -q PONG; do sleep 2; done'
    
    success "Infrastructure services are ready"
}

# Run database migrations
run_migrations() {
    log "Running database migrations..."
    
    # Run migrations using a temporary container
    docker-compose -f docker-compose.local.yml run --rm app java -jar /app/target/*.jar \
        --spring.profiles.active=local \
        --spring.liquibase.enabled=true \
        --spring.jpa.hibernate.ddl-auto=validate
    
    success "Database migrations completed"
}

# Start application
start_application() {
    log "Starting Spring Boot application..."
    
    # Start the application
    docker-compose -f docker-compose.local.yml up -d app
    
    # Wait for application to be healthy
    log "Waiting for application to be ready..."
    timeout 120 bash -c 'until curl -f http://localhost:${LOCAL_PORT}/management/health &>/dev/null; do sleep 5; done'
    
    success "Application is ready"
}

# Start management UIs
start_management_uis() {
    log "Starting management UIs..."
    
    # Start pgAdmin, Redis Commander, and MailHog
    docker-compose -f docker-compose.local.yml up -d pgadmin redis-commander mailhog
    
    success "Management UIs are ready"
}

# Create test data
create_test_data() {
    log "Creating test data..."
    
    # Create test tenants and users
    docker-compose -f docker-compose.local.yml exec postgres psql -U postgres -d malayalees_us_site -c "
        INSERT INTO tenant_organization (id, tenant_id, organization_name, domain_name, is_active, created_at, updated_at)
        VALUES 
            (1, 'tenant_demo_001', 'Demo Organization 1', 'demo1.local', true, NOW(), NOW()),
            (2, 'tenant_demo_002', 'Demo Organization 2', 'demo2.local', true, NOW(), NOW()),
            (3, 'tenant_demo_003', 'Demo Organization 3', 'demo3.local', true, NOW(), NOW())
        ON CONFLICT (id) DO NOTHING;
    "
    
    success "Test data created"
}

# Run tests
run_tests() {
    log "Running application tests..."
    
    # Run unit tests
    ./mvnw test -q
    
    success "Tests completed successfully"
}

# Health check
health_check() {
    log "Performing comprehensive health check..."
    
    # Application health
    if curl -f http://localhost:${LOCAL_PORT}/management/health &>/dev/null; then
        success "Application health check passed"
    else
        error "Application health check failed"
    fi
    
    # Database connectivity
    if docker-compose -f docker-compose.local.yml exec postgres pg_isready -U postgres -d malayalees_us_site &>/dev/null; then
        success "Database connectivity check passed"
    else
        error "Database connectivity check failed"
    fi
    
    # Redis connectivity
    if docker-compose -f docker-compose.local.yml exec redis redis-cli ping | grep -q PONG; then
        success "Redis connectivity check passed"
    else
        error "Redis connectivity check failed"
    fi
    
    success "All health checks passed"
}

# Show status
show_status() {
    log "Local Environment Status"
    echo "=================================="
    echo -e "${GREEN}✅ Application:${NC} http://localhost:${LOCAL_PORT}"
    echo -e "${GREEN}✅ Database:${NC} localhost:${POSTGRES_PORT}"
    echo -e "${GREEN}✅ Redis:${NC} localhost:${REDIS_PORT}"
    echo -e "${GREEN}✅ pgAdmin:${NC} http://localhost:${PGADMIN_PORT} (admin@local.com/admin)"
    echo -e "${GREEN}✅ Redis Commander:${NC} http://localhost:${REDIS_COMMANDER_PORT} (admin/admin)"
    echo -e "${GREEN}✅ MailHog:${NC} http://localhost:${MAILHOG_PORT}"
    echo ""
    echo -e "${BLUE}📊 API Endpoints:${NC}"
    echo "  - Health: http://localhost:${LOCAL_PORT}/management/health"
    echo "  - Metrics: http://localhost:${LOCAL_PORT}/management/metrics"
    echo "  - Swagger: http://localhost:${LOCAL_PORT}/swagger-ui.html"
    echo ""
    echo -e "${BLUE}🧪 Test Tenants:${NC}"
    echo "  - tenant_demo_001 (demo1.local)"
    echo "  - tenant_demo_002 (demo2.local)"
    echo "  - tenant_demo_003 (demo3.local)"
}

# Cleanup function
cleanup() {
    log "Cleaning up local environment..."
    
    # Stop and remove containers
    docker-compose -f docker-compose.local.yml down -v
    
    # Remove unused images (optional)
    # docker image prune -f
    
    success "Cleanup completed"
}

# Stop services
stop_services() {
    log "Stopping services..."
    
    docker-compose -f docker-compose.local.yml down
    
    success "Services stopped"
}

# Show logs
show_logs() {
    local service=${1:-app}
    
    log "Showing logs for service: $service"
    
    docker-compose -f docker-compose.local.yml logs -f "$service"
}

# Main function
main() {
    log "🚀 Starting Local Testing Environment Setup"
    
    check_prerequisites
    build_application
    start_infrastructure
    run_migrations
    create_test_data
    start_application
    start_management_uis
    run_tests
    health_check
    show_status
    
    success "🎉 Local environment is ready for testing!"
    echo ""
    info "To view logs: ./scripts/local-test.sh logs [service]"
    info "To stop services: ./scripts/local-test.sh stop"
    info "To cleanup: ./scripts/local-test.sh cleanup"
}

# Script entry point
case "${1:-start}" in
    "start")
        main
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        stop_services
        sleep 2
        main
        ;;
    "cleanup")
        cleanup
        ;;
    "logs")
        show_logs "$2"
        ;;
    "health")
        health_check
        ;;
    "status")
        show_status
        ;;
    "test")
        run_tests
        ;;
    "migrate")
        run_migrations
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|cleanup|logs|health|status|test|migrate}"
        echo ""
        echo "Commands:"
        echo "  start    - Start complete local environment (default)"
        echo "  stop     - Stop all services"
        echo "  restart  - Restart all services"
        echo "  cleanup  - Stop services and remove volumes"
        echo "  logs     - Show logs for a service (app|postgres|redis)"
        echo "  health   - Run health checks"
        echo "  status   - Show environment status"
        echo "  test     - Run application tests"
        echo "  migrate  - Run database migrations only"
        echo ""
        echo "Examples:"
        echo "  $0 start"
        echo "  $0 logs app"
        echo "  $0 cleanup"
        exit 1
        ;;
esac
