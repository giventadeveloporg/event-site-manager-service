@echo off
REM ===================================================================
REM Local Testing Script for Multi-Tenant Spring Boot Application (Windows)
REM ===================================================================
REM This script sets up and tests the complete local environment
REM including PostgreSQL, Redis, and the Spring Boot application.
REM ===================================================================

setlocal enabledelayedexpansion

REM Configuration
set APPLICATION_NAME=malayalees-us-site-boot
set LOCAL_PORT=8080
set POSTGRES_PORT=5432
set REDIS_PORT=6379
set PGADMIN_PORT=5050
set REDIS_COMMANDER_PORT=8081
set MAILHOG_PORT=8025

REM Color codes (Windows doesn't support colors in batch, but we can use echo)
set SUCCESS=[SUCCESS]
set WARNING=[WARNING]
set ERROR=[ERROR]
set INFO=[INFO]

REM Check prerequisites
:check_prerequisites
echo [%date% %time%] Checking prerequisites...

REM Check Docker
docker --version >nul 2>&1
if errorlevel 1 (
    echo %ERROR% Docker is not installed. Please install Docker first.
    exit /b 1
)

REM Check Docker Compose
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo %ERROR% Docker Compose is not installed. Please install Docker Compose first.
    exit /b 1
)

REM Check Maven
mvn --version >nul 2>&1
if errorlevel 1 (
    echo %ERROR% Maven is not installed. Please install Maven first.
    exit /b 1
)

REM Check Java
java -version >nul 2>&1
if errorlevel 1 (
    echo %ERROR% Java is not installed. Please install Java 11+ first.
    exit /b 1
)

REM Check Docker daemon
docker info >nul 2>&1
if errorlevel 1 (
    echo %ERROR% Docker daemon is not running. Please start Docker first.
    exit /b 1
)

echo %SUCCESS% Prerequisites check passed
goto :build_application

REM Build application
:build_application
echo [%date% %time%] Building Spring Boot application...

REM Clean and build
call mvnw.cmd clean package -DskipTests -q

if not exist "target\*.jar" (
    echo %ERROR% Build failed. JAR file not found.
    exit /b 1
)

echo %SUCCESS% Application built successfully
goto :start_infrastructure

REM Start infrastructure services
:start_infrastructure
echo [%date% %time%] Starting infrastructure services (PostgreSQL, Redis)...

REM Start only infrastructure services first
docker-compose -f docker-compose.local.yml up -d postgres redis

REM Wait for services to be ready
echo [%date% %time%] Waiting for PostgreSQL to be ready...
:wait_postgres
docker-compose -f docker-compose.local.yml exec postgres pg_isready -U postgres -d malayalees_us_site >nul 2>&1
if errorlevel 1 (
    timeout /t 2 /nobreak >nul
    goto :wait_postgres
)

echo [%date% %time%] Waiting for Redis to be ready...
:wait_redis
docker-compose -f docker-compose.local.yml exec redis redis-cli ping | findstr "PONG" >nul 2>&1
if errorlevel 1 (
    timeout /t 2 /nobreak >nul
    goto :wait_redis
)

echo %SUCCESS% Infrastructure services are ready
goto :run_migrations

REM Run database migrations
:run_migrations
echo [%date% %time%] Running database migrations...

REM Run migrations using a temporary container
docker-compose -f docker-compose.local.yml run --rm app java -jar /app/target/*.jar --spring.profiles.active=local --spring.liquibase.enabled=true --spring.jpa.hibernate.ddl-auto=validate

echo %SUCCESS% Database migrations completed
goto :create_test_data

REM Create test data
:create_test_data
echo [%date% %time%] Creating test data...

REM Create test tenants and users
docker-compose -f docker-compose.local.yml exec postgres psql -U postgres -d malayalees_us_site -c "INSERT INTO tenant_organization (id, tenant_id, organization_name, domain_name, is_active, created_at, updated_at) VALUES (1, 'tenant_demo_001', 'Demo Organization 1', 'demo1.local', true, NOW(), NOW()), (2, 'tenant_demo_002', 'Demo Organization 2', 'demo2.local', true, NOW(), NOW()), (3, 'tenant_demo_003', 'Demo Organization 3', 'demo3.local', true, NOW(), NOW()) ON CONFLICT (id) DO NOTHING;"

echo %SUCCESS% Test data created
goto :start_application

REM Start application
:start_application
echo [%date% %time%] Starting Spring Boot application...

REM Start the application
docker-compose -f docker-compose.local.yml up -d app

REM Wait for application to be healthy
echo [%date% %time%] Waiting for application to be ready...
:wait_app
curl -f http://localhost:%LOCAL_PORT%/management/health >nul 2>&1
if errorlevel 1 (
    timeout /t 5 /nobreak >nul
    goto :wait_app
)

echo %SUCCESS% Application is ready
goto :start_management_uis

REM Start management UIs
:start_management_uis
echo [%date% %time%] Starting management UIs...

REM Start pgAdmin, Redis Commander, and MailHog
docker-compose -f docker-compose.local.yml up -d pgadmin redis-commander mailhog

echo %SUCCESS% Management UIs are ready
goto :run_tests

REM Run tests
:run_tests
echo [%date% %time%] Running application tests...

REM Run unit tests
call mvnw.cmd test -q

echo %SUCCESS% Tests completed successfully
goto :health_check

REM Health check
:health_check
echo [%date% %time%] Performing comprehensive health check...

REM Application health
curl -f http://localhost:%LOCAL_PORT%/management/health >nul 2>&1
if errorlevel 1 (
    echo %ERROR% Application health check failed
    exit /b 1
) else (
    echo %SUCCESS% Application health check passed
)

REM Database connectivity
docker-compose -f docker-compose.local.yml exec postgres pg_isready -U postgres -d malayalees_us_site >nul 2>&1
if errorlevel 1 (
    echo %ERROR% Database connectivity check failed
    exit /b 1
) else (
    echo %SUCCESS% Database connectivity check passed
)

REM Redis connectivity
docker-compose -f docker-compose.local.yml exec redis redis-cli ping | findstr "PONG" >nul 2>&1
if errorlevel 1 (
    echo %ERROR% Redis connectivity check failed
    exit /b 1
) else (
    echo %SUCCESS% Redis connectivity check passed
)

echo %SUCCESS% All health checks passed
goto :show_status

REM Show status
:show_status
echo [%date% %time%] Local Environment Status
echo ==================================
echo %SUCCESS% Application: http://localhost:%LOCAL_PORT%
echo %SUCCESS% Database: localhost:%POSTGRES_PORT%
echo %SUCCESS% Redis: localhost:%REDIS_PORT%
echo %SUCCESS% pgAdmin: http://localhost:%PGADMIN_PORT% (admin@local.com/admin)
echo %SUCCESS% Redis Commander: http://localhost:%REDIS_COMMANDER_PORT% (admin/admin)
echo %SUCCESS% MailHog: http://localhost:%MAILHOG_PORT%
echo.
echo %INFO% API Endpoints:
echo   - Health: http://localhost:%LOCAL_PORT%/management/health
echo   - Metrics: http://localhost:%LOCAL_PORT%/management/metrics
echo   - Swagger: http://localhost:%LOCAL_PORT%/swagger-ui.html
echo.
echo %INFO% Test Tenants:
echo   - tenant_demo_001 (demo1.local)
echo   - tenant_demo_002 (demo2.local)
echo   - tenant_demo_003 (demo3.local)
goto :end

REM Stop services
:stop_services
echo [%date% %time%] Stopping services...
docker-compose -f docker-compose.local.yml down
echo %SUCCESS% Services stopped
goto :end

REM Cleanup
:cleanup
echo [%date% %time%] Cleaning up local environment...
docker-compose -f docker-compose.local.yml down -v
echo %SUCCESS% Cleanup completed
goto :end

REM Show logs
:show_logs
set service=%2
if "%service%"=="" set service=app
echo [%date% %time%] Showing logs for service: %service%
docker-compose -f docker-compose.local.yml logs -f %service%
goto :end

REM Main function
:main
echo [%date% %time%] Starting Local Testing Environment Setup
call :check_prerequisites
call :build_application
call :start_infrastructure
call :run_migrations
call :create_test_data
call :start_application
call :start_management_uis
call :run_tests
call :health_check
call :show_status
echo %SUCCESS% Local environment is ready for testing!
echo.
echo %INFO% To view logs: scripts\local-test.bat logs [service]
echo %INFO% To stop services: scripts\local-test.bat stop
echo %INFO% To cleanup: scripts\local-test.bat cleanup
goto :end

REM Script entry point
if "%1"=="" goto :main
if "%1"=="start" goto :main
if "%1"=="stop" goto :stop_services
if "%1"=="restart" (
    call :stop_services
    timeout /t 2 /nobreak >nul
    goto :main
)
if "%1"=="cleanup" goto :cleanup
if "%1"=="logs" goto :show_logs
if "%1"=="health" goto :health_check
if "%1"=="status" goto :show_status
if "%1"=="test" goto :run_tests
if "%1"=="migrate" goto :run_migrations

echo Usage: %0 {start^|stop^|restart^|cleanup^|logs^|health^|status^|test^|migrate}
echo.
echo Commands:
echo   start    - Start complete local environment (default)
echo   stop     - Stop all services
echo   restart  - Restart all services
echo   cleanup  - Stop services and remove volumes
echo   logs     - Show logs for a service (app^|postgres^|redis)
echo   health   - Run health checks
echo   status   - Show environment status
echo   test     - Run application tests
echo   migrate  - Run database migrations only
echo.
echo Examples:
echo   %0 start
echo   %0 logs app
echo   %0 cleanup
exit /b 1

:end
endlocal
