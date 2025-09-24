# AWS Fargate Deployment Guide - Complete Step-by-Step

## Table of Contents
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [PostgreSQL Database Setup](#postgresql-database-setup)
4. [Fargate Deployment](#fargate-deployment)
5. [Environment Variables Management](#environment-variables-management)
6. [Monitoring & Logging](#monitoring--logging)
7. [Troubleshooting](#troubleshooting)
8. [Cost Optimization](#cost-optimization)

---

## Overview

This guide provides step-by-step instructions for deploying your Spring Boot application to AWS Fargate with PostgreSQL caching, based on your successful Docker local setup.

### Architecture Overview
```mermaid
graph TB
    ALB[Application Load Balancer]
    ECS[ECS Fargate Cluster]
    TASK1[Fargate Task 1]
    TASK2[Fargate Task 2]
    RDS[RDS PostgreSQL<br/>with Built-in Caching]
    ECR[ECR Repository]
    SM[AWS Secrets Manager]

    ALB --> ECS
    ECS --> TASK1
    ECS --> TASK2
    TASK1 --> RDS
    TASK2 --> RDS
    ECR --> TASK1
    ECR --> TASK2
    SM --> TASK1
    SM --> TASK2
```

### Key Benefits
- ✅ **No Server Management**: AWS handles infrastructure
- ✅ **Auto Scaling**: Automatic container scaling based on demand
- ✅ **Cost Effective**: Pay only for running containers
- ✅ **PostgreSQL Caching**: Built-in caching eliminates Redis dependency
- ✅ **High Availability**: Multi-AZ deployment with load balancing

---

## Prerequisites

### 1. AWS Account Setup
- AWS Account with appropriate permissions
- AWS CLI installed and configured
- Docker installed locally
- Maven installed locally

### 2. Required AWS Services
- **ECS (Elastic Container Service)**
- **Fargate**
- **ECR (Elastic Container Registry)**
- **RDS (Relational Database Service)**
- **Application Load Balancer**
- **Secrets Manager**
- **CloudWatch**
- **IAM**

### 3. Local Environment Verification
Ensure your local Docker setup is working:
```bash
cd src/main/docker/Docker_Fargate
docker-compose -f docker-compose.local.yml up --build
```

---

## PostgreSQL Database Setup

### Step 1: Create RDS Parameter Group

Create a custom parameter group optimized for caching:

```bash
# Create parameter group
aws rds create-db-parameter-group \
    --db-parameter-group-name malayalees-postgres-cache \
    --db-parameter-group-family postgres13 \
    --description "PostgreSQL parameter group optimized for caching"

# Set caching parameters
aws rds modify-db-parameter-group \
    --db-parameter-group-name malayalees-postgres-cache \
    --parameters \
        ParameterName=shared_buffers,ParameterValue=2GB,ApplyMethod=immediate \
        ParameterName=effective_cache_size,ParameterValue=6GB,ApplyMethod=immediate \
        ParameterName=work_mem,ParameterValue=64MB,ApplyMethod=immediate \
        ParameterName=maintenance_work_mem,ParameterValue=512MB,ApplyMethod=immediate \
        ParameterName=shared_preload_libraries,ParameterValue=pg_stat_statements,ApplyMethod=immediate \
        ParameterName=max_connections,ParameterValue=200,ApplyMethod=immediate
```

### Step 2: Create RDS Subnet Group

```bash
# Create subnet group (replace with your subnet IDs)
aws rds create-db-subnet-group \
    --db-subnet-group-name malayalees-subnet-group \
    --db-subnet-group-description "Subnet group for Malayalees RDS" \
    --subnet-ids subnet-12345678 subnet-87654321
```

### Step 3: Create RDS Instance

```bash
# Create RDS instance
aws rds create-db-instance \
    --db-instance-identifier malayalees-postgres \
    --db-instance-class db.t3.micro \
    --engine postgres \
    --engine-version 13.7 \
    --master-username malayalees_admin \
    --master-user-password YourSecurePassword123! \
    --allocated-storage 20 \
    --storage-type gp2 \
    --vpc-security-group-ids sg-12345678 \
    --db-subnet-group-name malayalees-subnet-group \
    --db-parameter-group-name malayalees-postgres-cache \
    --backup-retention-period 7 \
    --multi-az \
    --storage-encrypted \
    --deletion-protection
```

### Step 4: Create Database and User

```bash
# Connect to RDS instance
psql -h your-rds-endpoint.rds.amazonaws.com -U malayalees_admin -d postgres

# Create application database
CREATE DATABASE malayalees_us_site;

# Create application user
CREATE USER malayalees_app WITH PASSWORD 'YourAppPassword123!';

# Grant permissions
GRANT ALL PRIVILEGES ON DATABASE malayalees_us_site TO malayalees_app;
GRANT ALL PRIVILEGES ON SCHEMA public TO malayalees_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO malayalees_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO malayalees_app;
```

### Step 5: Setup PostgreSQL Caching Tables

```sql
-- Connect to application database
\c malayalees_us_site;

-- Create application cache table
CREATE UNLOGGED TABLE IF NOT EXISTS app_cache (
    cache_key TEXT PRIMARY KEY,
    cache_value TEXT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create session store table
CREATE TABLE IF NOT EXISTS session_store (
    session_id VARCHAR(255) PRIMARY KEY,
    session_data BYTEA,
    last_access_time TIMESTAMP DEFAULT NOW(),
    max_inactive_interval INTEGER DEFAULT 1800
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_app_cache_expires_at ON app_cache(expires_at);
CREATE INDEX IF NOT EXISTS idx_session_store_last_access ON session_store(last_access_time);

-- Create cache cleanup function
CREATE OR REPLACE FUNCTION cleanup_expired_cache()
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM app_cache WHERE expires_at < NOW();
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Create session cleanup function
CREATE OR REPLACE FUNCTION cleanup_expired_sessions()
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM session_store
    WHERE last_access_time < NOW() - INTERVAL '1 hour' * max_inactive_interval;
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Grant permissions to application user
GRANT SELECT, INSERT, UPDATE, DELETE ON app_cache TO malayalees_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON session_store TO malayalees_app;
GRANT EXECUTE ON FUNCTION cleanup_expired_cache() TO malayalees_app;
GRANT EXECUTE ON FUNCTION cleanup_expired_sessions() TO malayalees_app;
```

### Step 6: Database Migration

```bash
# Run database migrations
java -jar target/nextjs-template-boot-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=prod-aws-postgres-cache \
    --spring.datasource.url=jdbc:postgresql://your-rds-endpoint.rds.amazonaws.com:5432/malayalees_us_site \
    --spring.datasource.username=malayalees_app \
    --spring.datasource.password=YourAppPassword123! \
    --spring.liquibase.enabled=true
```

---

## Fargate Deployment

### Step 1: Create ECR Repository

```bash
# Create ECR repository
aws ecr create-repository \
    --repository-name malayalees-app \
    --image-scanning-configuration scanOnPush=true

# Get login token
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789012.dkr.ecr.us-east-1.amazonaws.com
```

### Step 2: Build and Push Docker Image

```bash
# Build application
./mvnw clean package -DskipTests

# Build Docker image
docker build -f src/main/docker/Docker_Fargate/Dockerfile.local -t malayalees-app .

# Tag image
docker tag malayalees-app:latest 123456789012.dkr.ecr.us-east-1.amazonaws.com/malayalees-app:latest

# Push image
docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/malayalees-app:latest
```

### Step 3: Create IAM Roles

```bash
# Create ECS task execution role
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

# Attach managed policy
aws iam attach-role-policy \
    --role-name ecsTaskExecutionRole \
    --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy

# Create ECS task role
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
```

### Step 4: Create ECS Cluster

```bash
# Create ECS cluster
aws ecs create-cluster \
    --cluster-name malayalees-cluster \
    --capacity-providers FARGATE \
    --default-capacity-provider-strategy capacityProvider=FARGATE,weight=1
```

### Step 5: Create Task Definition

```json
{
  "family": "malayalees-app",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::123456789012:role/ecsTaskExecutionRole",
  "taskRoleArn": "arn:aws:iam::123456789012:role/ecsTaskRole",
  "containerDefinitions": [
    {
      "name": "malayalees-app",
      "image": "123456789012.dkr.ecr.us-east-1.amazonaws.com/malayalees-app:latest",
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
          "value": "your-rds-endpoint.rds.amazonaws.com"
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
          "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/db-password"
        },
        {
          "name": "JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/jwt-secret"
        },
        {
          "name": "JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/jwt-base64-secret"
        },
        {
          "name": "AWS_ACCESS_KEY_ID",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/aws-access-key"
        },
        {
          "name": "AWS_SECRET_ACCESS_KEY",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/aws-secret-key"
        },
        {
          "name": "TWILIO_ACCOUNT_SID",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/twilio-account-sid"
        },
        {
          "name": "TWILIO_AUTH_TOKEN",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/twilio-auth-token"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/malayalees-app",
          "awslogs-region": "us-east-1",
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
```

```bash
# Register task definition
aws ecs register-task-definition --cli-input-json file://task-definition.json
```

### Step 6: Create Application Load Balancer

```bash
# Create target group
aws elbv2 create-target-group \
    --name malayalees-targets \
    --protocol HTTP \
    --port 8080 \
    --vpc-id vpc-12345678 \
    --target-type ip \
    --health-check-path /management/health \
    --health-check-interval-seconds 30 \
    --health-check-timeout-seconds 5 \
    --healthy-threshold-count 2 \
    --unhealthy-threshold-count 3

# Create load balancer
aws elbv2 create-load-balancer \
    --name malayalees-alb \
    --subnets subnet-12345678 subnet-87654321 \
    --security-groups sg-12345678 \
    --scheme internet-facing \
    --type application \
    --ip-address-type ipv4

# Create listener
aws elbv2 create-listener \
    --load-balancer-arn arn:aws:elasticloadbalancing:us-east-1:123456789012:loadbalancer/app/malayalees-alb/1234567890123456 \
    --protocol HTTP \
    --port 80 \
    --default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:us-east-1:123456789012:targetgroup/malayalees-targets/1234567890123456
```

### Step 7: Create ECS Service

```bash
# Create ECS service
aws ecs create-service \
    --cluster malayalees-cluster \
    --service-name malayalees-service \
    --task-definition malayalees-app:1 \
    --desired-count 2 \
    --launch-type FARGATE \
    --platform-version LATEST \
    --network-configuration "awsvpcConfiguration={subnets=[subnet-12345678,subnet-87654321],securityGroups=[sg-12345678],assignPublicIp=ENABLED}" \
    --load-balancers "targetGroupArn=arn:aws:elasticloadbalancing:us-east-1:123456789012:targetgroup/malayalees-targets/1234567890123456,containerName=malayalees-app,containerPort=8080" \
    --enable-execute-command \
    --enable-logging
```

---

## Environment Variables Management

### Step 1: Create AWS Secrets Manager Secrets

```bash
# Create database password secret
aws secretsmanager create-secret \
    --name malayalees/db-password \
    --description "Database password for Malayalees application" \
    --secret-string "YourAppPassword123!"

# Create JWT secret
aws secretsmanager create-secret \
    --name malayalees/jwt-secret \
    --description "JWT secret for Malayalees application" \
    --secret-string "mySecretKey12345678901234567890123456789012345678901234567890"

# Create JWT base64 secret
aws secretsmanager create-secret \
    --name malayalees/jwt-base64-secret \
    --description "JWT base64 secret for Malayalees application" \
    --secret-string "bXlTZWNyZXRLZXkxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA=="

# Create AWS credentials
aws secretsmanager create-secret \
    --name malayalees/aws-access-key \
    --description "AWS access key for Malayalees application" \
    --secret-string "AKIATIT5HARDKCWNLQMU"

aws secretsmanager create-secret \
    --name malayalees/aws-secret-key \
    --description "AWS secret key for Malayalees application" \
    --secret-string "9xyoyfvKjMJzRhDBZEkqM/qatrGUtV4IVO6CuIBo"

# Create Twilio credentials
aws secretsmanager create-secret \
    --name malayalees/twilio-account-sid \
    --description "Twilio account SID for Malayalees application" \
    --secret-string "AC48380299acc5e7e27aee75a3108c3058"

aws secretsmanager create-secret \
    --name malayalees/twilio-auth-token \
    --description "Twilio auth token for Malayalees application" \
    --secret-string "f460b6085002a38cf3c5cadd61c21057"
```

### Step 2: Create Application Configuration Profile

Create `src/main/resources/config/application-prod-aws-postgres-cache.yml`:

```yaml
spring:
  profiles:
    active: prod-aws-postgres-cache

  datasource:
    url: jdbc:postgresql://${RDS_ENDPOINT}:5432/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000

  # PostgreSQL-based Caching Configuration
  cache:
    type: postgresql
    postgresql:
      cache-table: ${POSTGRES_CACHE_TABLE:app_cache}
      time-to-live: 600000 # 10 minutes
      cache-null-values: false
      enable-statistics: true

  # Session Management with PostgreSQL
  session:
    store-type: postgresql
    postgresql:
      table-name: "session_store"
      cleanup-cron: "0 0 * * * *" # Every hour

  # JPA/Hibernate with PostgreSQL caching
  jpa:
    properties:
      hibernate:
        cache:
          use_second_level_cache: true
          use_query_cache: true
          region:
            factory_class: org.hibernate.cache.jcache.JCacheRegionFactory
        connection:
          provider_disables_autocommit: true

  liquibase:
    enabled: true
    contexts: prod-aws-postgres-cache

server:
  port: 8080

logging:
  level:
    ROOT: INFO
    com.nextjstemplate: INFO
    org.hibernate.SQL: WARN
    org.hibernate.cache: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,cache
  endpoint:
    health:
      show-details: when-authorized
  cache:
    cache-manager: postgresqlCacheManager

# JHipster Security Configuration
jhipster:
  security:
    authentication:
      jwt:
        secret: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET}
        base64-secret: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET}
        token-validity-in-seconds: 86400
        token-validity-in-seconds-for-remember-me: 2592000
  cache:
    hazelcast:
      time-to-live-seconds: 3600
      backup-count: 1
      eviction:
        max-size: 100
        max-size-policy: PER_NODE

# AWS Configuration
aws:
  access-key-id: ${AWS_ACCESS_KEY_ID}
  secret-access-key: ${AWS_SECRET_ACCESS_KEY}
  region: us-east-1

# Twilio Configuration
twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}
  auth-token: ${TWILIO_AUTH_TOKEN}
  whatsapp-from: ${TWILIO_WHATSAPP_FROM:whatsapp:+14155238886}

# Application URLs
application:
  urls:
    qrcode-scan-host-prefix: ${QRCODE_SCAN_HOST_PREFIX:https://your-domain.com}
    email-host-url-prefix: ${EMAIL_HOST_URL_PREFIX:https://your-domain.com}
```

### Step 3: Environment Variables Mapping

| **Environment Variable** | **AWS Secrets Manager** | **Purpose** |
|---------------------------|--------------------------|-------------|
| `DB_PASSWORD` | `malayalees/db-password` | Database password |
| `JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET` | `malayalees/jwt-secret` | JWT signing secret |
| `JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET` | `malayalees/jwt-base64-secret` | JWT base64 secret |
| `AWS_ACCESS_KEY_ID` | `malayalees/aws-access-key` | AWS access key |
| `AWS_SECRET_ACCESS_KEY` | `malayalees/aws-secret-key` | AWS secret key |
| `TWILIO_ACCOUNT_SID` | `malayalees/twilio-account-sid` | Twilio account SID |
| `TWILIO_AUTH_TOKEN` | `malayalees/twilio-auth-token` | Twilio auth token |

---

## Monitoring & Logging

### Step 1: Create CloudWatch Log Group

```bash
# Create log group
aws logs create-log-group \
    --log-group-name /ecs/malayalees-app \
    --retention-in-days 30
```

### Step 2: Create CloudWatch Alarms

```bash
# Create CPU utilization alarm
aws cloudwatch put-metric-alarm \
    --alarm-name malayalees-cpu-high \
    --alarm-description "High CPU utilization" \
    --metric-name CPUUtilization \
    --namespace AWS/ECS \
    --statistic Average \
    --period 300 \
    --threshold 80 \
    --comparison-operator GreaterThanThreshold \
    --evaluation-periods 2 \
    --alarm-actions arn:aws:sns:us-east-1:123456789012:malayalees-alerts

# Create memory utilization alarm
aws cloudwatch put-metric-alarm \
    --alarm-name malayalees-memory-high \
    --alarm-description "High memory utilization" \
    --metric-name MemoryUtilization \
    --namespace AWS/ECS \
    --statistic Average \
    --period 300 \
    --threshold 80 \
    --comparison-operator GreaterThanThreshold \
    --evaluation-periods 2 \
    --alarm-actions arn:aws:sns:us-east-1:123456789012:malayalees-alerts
```

### Step 3: Database Monitoring

```bash
# Create RDS monitoring alarm
aws cloudwatch put-metric-alarm \
    --alarm-name malayalees-rds-cpu-high \
    --alarm-description "High RDS CPU utilization" \
    --metric-name CPUUtilization \
    --namespace AWS/RDS \
    --statistic Average \
    --period 300 \
    --threshold 80 \
    --comparison-operator GreaterThanThreshold \
    --evaluation-periods 2 \
    --dimensions Name=DBInstanceIdentifier,Value=malayalees-postgres \
    --alarm-actions arn:aws:sns:us-east-1:123456789012:malayalees-alerts
```

---

## Troubleshooting

### Common Issues and Solutions

#### 1. Container Fails to Start
```bash
# Check ECS service logs
aws logs describe-log-streams --log-group-name /ecs/malayalees-app

# Check specific log stream
aws logs get-log-events --log-group-name /ecs/malayalees-app --log-stream-name ecs/malayalees-app/task-id
```

#### 2. Database Connection Issues
```bash
# Test database connectivity
aws ecs execute-command \
    --cluster malayalees-cluster \
    --task task-id \
    --container malayalees-app \
    --interactive \
    --command "/bin/bash"

# Inside container, test connection
psql -h your-rds-endpoint.rds.amazonaws.com -U malayalees_app -d malayalees_us_site
```

#### 3. Environment Variables Not Loading
```bash
# Check secrets in AWS Secrets Manager
aws secretsmanager get-secret-value --secret-id malayalees/db-password

# Verify task definition
aws ecs describe-task-definition --task-definition malayalees-app:1
```

#### 4. Load Balancer Health Check Failures
```bash
# Check target group health
aws elbv2 describe-target-health --target-group-arn arn:aws:elasticloadbalancing:us-east-1:123456789012:targetgroup/malayalees-targets/1234567890123456

# Check application health endpoint
curl -f http://your-alb-endpoint.us-east-1.elb.amazonaws.com/management/health
```

---

## Cost Optimization

### 1. Right-Sizing Resources
- Start with `db.t3.micro` for RDS
- Use 0.5 vCPU and 1GB RAM for Fargate tasks
- Monitor usage and scale up as needed

### 2. Auto Scaling Configuration
```bash
# Create auto scaling target
aws application-autoscaling register-scalable-target \
    --service-namespace ecs \
    --resource-id service/malayalees-cluster/malayalees-service \
    --scalable-dimension ecs:service:DesiredCount \
    --min-capacity 1 \
    --max-capacity 10

# Create scaling policy
aws application-autoscaling put-scaling-policy \
    --service-namespace ecs \
    --resource-id service/malayalees-cluster/malayalees-service \
    --scalable-dimension ecs:service:DesiredCount \
    --policy-name malayalees-cpu-scaling \
    --policy-type TargetTrackingScaling \
    --target-tracking-scaling-policy-configuration '{
        "TargetValue": 70.0,
        "PredefinedMetricSpecification": {
            "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
        },
        "ScaleOutCooldown": 300,
        "ScaleInCooldown": 300
    }'
```

### 3. Cost Monitoring
```bash
# Create cost anomaly detection
aws ce create-anomaly-detector \
    --anomaly-detector-type DAILY \
    --anomaly-detector-name malayalees-cost-anomaly \
    --monitor-dimension SERVICE
```

---

## Read-Only Database Consideration

### Do You Need a Read-Only Database?

Based on your architecture, **you likely DON'T need a separate read-only database** because:

1. **PostgreSQL Built-in Caching**: Your RDS instance already has optimized caching
2. **Connection Pooling**: HikariCP handles connection management efficiently
3. **Application-Level Caching**: Spring Cache with PostgreSQL provides additional caching
4. **Cost Efficiency**: Single database with caching is more cost-effective

### When to Consider Read Replicas

Consider read replicas only if:
- **High Read Volume**: >10,000 reads/second
- **Geographic Distribution**: Users in multiple regions
- **Reporting Workloads**: Heavy analytical queries
- **Disaster Recovery**: Cross-region backup requirements

### Read Replica Setup (If Needed)

```bash
# Create read replica
aws rds create-db-instance-read-replica \
    --db-instance-identifier malayalees-postgres-read-replica \
    --source-db-instance-identifier malayalees-postgres \
    --db-instance-class db.t3.micro \
    --publicly-accessible \
    --storage-encrypted
```

---

## Deployment Checklist

### Pre-Deployment
- [ ] AWS CLI configured
- [ ] Docker image built and tested locally
- [ ] Database schema created
- [ ] Secrets stored in AWS Secrets Manager
- [ ] IAM roles created

### Deployment
- [ ] ECR repository created
- [ ] Docker image pushed to ECR
- [ ] ECS cluster created
- [ ] Task definition registered
- [ ] Load balancer created
- [ ] ECS service created

### Post-Deployment
- [ ] Health checks passing
- [ ] Application accessible via load balancer
- [ ] Database connectivity verified
- [ ] Environment variables loaded correctly
- [ ] Monitoring and alerts configured
- [ ] Auto scaling configured

---

## Next Steps

1. **Deploy to Staging**: Test the complete setup in a staging environment
2. **Performance Testing**: Load test the application with PostgreSQL caching
3. **Monitoring Setup**: Configure comprehensive monitoring and alerting
4. **Documentation**: Update team documentation with deployment procedures
5. **Backup Strategy**: Implement automated backup and disaster recovery

---

*Last Updated: January 2025*
*Version: 1.0 - Complete AWS Fargate Deployment Guide*
