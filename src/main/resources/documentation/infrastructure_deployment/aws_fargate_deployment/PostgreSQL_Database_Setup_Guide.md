# PostgreSQL Database Setup Guide for AWS Fargate Deployment

## Table of Contents
1. [Overview](#overview)
2. [Database Architecture](#database-architecture)
3. [RDS Setup](#rds-setup)
4. [Database Configuration](#database-configuration)
5. [Caching Setup](#caching-setup)
6. [Read Replica Consideration](#read-replica-consideration)
7. [Monitoring & Maintenance](#monitoring--maintenance)
8. [Troubleshooting](#troubleshooting)

---

## Overview

This guide provides comprehensive instructions for setting up PostgreSQL database for your AWS Fargate deployment, including built-in caching features that eliminate the need for Redis.

### Key Benefits of PostgreSQL Caching
- ✅ **Cost Reduction**: Eliminate Redis/ElastiCache costs ($15-60/month)
- ✅ **Simplified Architecture**: Single database dependency
- ✅ **ACID Compliance**: Cache operations are transactional
- ✅ **Better Resource Utilization**: Shared memory pool
- ✅ **Easier Monitoring**: Single point of monitoring
- ✅ **Backup & Recovery**: Cache data included in database backups

---

## Database Architecture

### Single Database vs Read Replica Decision

Based on your current architecture and requirements, **you likely DON'T need a read replica** because:

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

---

## RDS Setup

### Step 1: Create RDS Parameter Group

Create a custom parameter group optimized for caching:

```bash
# Create parameter group
aws rds create-db-parameter-group \
    --db-parameter-group-name malayalees-postgres-cache \
    --db-parameter-group-family postgres13 \
    --description "PostgreSQL parameter group optimized for caching" \
    --region us-east-1

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
        ParameterName=random_page_cost,ParameterValue=1.1,ApplyMethod=immediate \
        ParameterName=effective_io_concurrency,ParameterValue=200,ApplyMethod=immediate \
    --region us-east-1
```

### Step 2: Create RDS Subnet Group

```bash
# Create subnet group (replace with your subnet IDs)
aws rds create-db-subnet-group \
    --db-subnet-group-name malayalees-subnet-group \
    --db-subnet-group-description "Subnet group for Malayalees RDS" \
    --subnet-ids subnet-12345678 subnet-87654321 \
    --region us-east-1
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
    --master-user-password "YourSecurePassword123!" \
    --allocated-storage 20 \
    --storage-type gp2 \
    --vpc-security-group-ids sg-12345678 \
    --db-subnet-group-name malayalees-subnet-group \
    --db-parameter-group-name malayalees-postgres-cache \
    --backup-retention-period 7 \
    --multi-az \
    --storage-encrypted \
    --deletion-protection \
    --region us-east-1
```

### Step 4: Wait for Instance to be Available

```bash
# Wait for RDS instance to be available
aws rds wait db-instance-available \
    --db-instance-identifier malayalees-postgres \
    --region us-east-1

# Get RDS endpoint
RDS_ENDPOINT=$(aws rds describe-db-instances \
    --db-instance-identifier malayalees-postgres \
    --region us-east-1 \
    --query 'DBInstances[0].Endpoint.Address' \
    --output text)

echo "RDS endpoint: $RDS_ENDPOINT"
```

---

## Database Configuration

### Step 1: Create Database and User

```bash
# Connect to RDS instance
psql -h $RDS_ENDPOINT -U malayalees_admin -d postgres

# Create application database
CREATE DATABASE malayalees_us_site;

# Create application user
CREATE USER malayalees_app WITH PASSWORD 'YourAppPassword123!';

# Grant permissions
GRANT ALL PRIVILEGES ON DATABASE malayalees_us_site TO malayalees_app;
GRANT ALL PRIVILEGES ON SCHEMA public TO malayalees_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO malayalees_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO malayalees_app;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO malayalees_app;

# Exit psql
\q
```

### Step 2: Test Connection

```bash
# Test connection with application user
psql -h $RDS_ENDPOINT -U malayalees_app -d malayalees_us_site

# Test basic functionality
SELECT version();
SELECT current_database();
SELECT current_user;

# Exit psql
\q
```

---

## Caching Setup

### Step 1: Create PostgreSQL Caching Tables

```sql
-- Connect to application database
\c malayalees_us_site;

-- Create application cache table (UNLOGGED for performance)
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

-- Grant permissions to application user
GRANT SELECT, INSERT, UPDATE, DELETE ON app_cache TO malayalees_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON session_store TO malayalees_app;
```

### Step 2: Create Cache Management Functions

```sql
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

-- Grant execute permissions
GRANT EXECUTE ON FUNCTION cleanup_expired_cache() TO malayalees_app;
GRANT EXECUTE ON FUNCTION cleanup_expired_sessions() TO malayalees_app;
```

### Step 3: Create Materialized Views for Complex Queries

```sql
-- Create materialized view for tenant statistics
CREATE MATERIALIZED VIEW tenant_stats AS
SELECT
    tenant_id,
    COUNT(*) as user_count,
    MAX(created_at) as last_activity,
    COUNT(CASE WHEN is_active = true THEN 1 END) as active_users
FROM user_profile
GROUP BY tenant_id;

-- Create index on materialized view
CREATE INDEX idx_tenant_stats_tenant_id ON tenant_stats(tenant_id);

-- Create function to refresh materialized view
CREATE OR REPLACE FUNCTION refresh_tenant_stats()
RETURNS VOID AS $$
BEGIN
    REFRESH MATERIALIZED VIEW tenant_stats;
END;
$$ LANGUAGE plpgsql;

-- Grant permissions
GRANT SELECT ON tenant_stats TO malayalees_app;
GRANT EXECUTE ON FUNCTION refresh_tenant_stats() TO malayalees_app;
```

### Step 4: Setup Automated Cleanup

```sql
-- Create cleanup job (requires pg_cron extension)
-- Note: This requires superuser privileges and pg_cron extension

-- Enable pg_cron extension (run as superuser)
CREATE EXTENSION IF NOT EXISTS pg_cron;

-- Schedule cache cleanup every hour
SELECT cron.schedule('cleanup-cache', '0 * * * *', 'SELECT cleanup_expired_cache();');

-- Schedule session cleanup every hour
SELECT cron.schedule('cleanup-sessions', '0 * * * *', 'SELECT cleanup_expired_sessions();');

-- Schedule materialized view refresh every 6 hours
SELECT cron.schedule('refresh-tenant-stats', '0 */6 * * *', 'SELECT refresh_tenant_stats();');
```

---

## Read Replica Setup (Optional)

### When to Create Read Replica

Create a read replica if you experience:
- **High Read Volume**: >10,000 reads/second
- **Geographic Distribution**: Users in multiple regions
- **Reporting Workloads**: Heavy analytical queries
- **Disaster Recovery**: Cross-region backup requirements

### Create Read Replica

```bash
# Create read replica
aws rds create-db-instance-read-replica \
    --db-instance-identifier malayalees-postgres-read-replica \
    --source-db-instance-identifier malayalees-postgres \
    --db-instance-class db.t3.micro \
    --publicly-accessible \
    --storage-encrypted \
    --region us-east-1

# Wait for read replica to be available
aws rds wait db-instance-available \
    --db-instance-identifier malayalees-postgres-read-replica \
    --region us-east-1
```

### Configure Application for Read Replica

```yaml
# application-prod-aws-postgres-cache.yml
spring:
  datasource:
    url: jdbc:postgresql://${RDS_ENDPOINT}:5432/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

  # Read replica configuration
  datasource:
    read:
      url: jdbc:postgresql://${RDS_READ_REPLICA_ENDPOINT}:5432/${DB_NAME}
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
      hikari:
        maximum-pool-size: 10
        minimum-idle: 2
```

---

## Monitoring & Maintenance

### Step 1: Create CloudWatch Alarms

```bash
# Create RDS CPU utilization alarm
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
    --alarm-actions arn:aws:sns:us-east-1:123456789012:malayalees-alerts \
    --region us-east-1

# Create RDS connection count alarm
aws cloudwatch put-metric-alarm \
    --alarm-name malayalees-rds-connections-high \
    --alarm-description "High RDS connection count" \
    --metric-name DatabaseConnections \
    --namespace AWS/RDS \
    --statistic Average \
    --period 300 \
    --threshold 150 \
    --comparison-operator GreaterThanThreshold \
    --evaluation-periods 2 \
    --dimensions Name=DBInstanceIdentifier,Value=malayalees-postgres \
    --alarm-actions arn:aws:sns:us-east-1:123456789012:malayalees-alerts \
    --region us-east-1
```

### Step 2: Setup Database Monitoring

```sql
-- Enable query statistics
-- This is already configured in the parameter group

-- Check current connections
SELECT
    datname,
    usename,
    application_name,
    client_addr,
    state,
    query_start,
    query
FROM pg_stat_activity
WHERE state = 'active';

-- Check cache hit ratio
SELECT
    schemaname,
    tablename,
    heap_blks_read,
    heap_blks_hit,
    round(heap_blks_hit::numeric / (heap_blks_hit + heap_blks_read) * 100, 2) as cache_hit_ratio
FROM pg_statio_user_tables
WHERE heap_blks_hit + heap_blks_read > 0;

-- Check slow queries
SELECT
    query,
    calls,
    total_time,
    mean_time,
    rows
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;
```

### Step 3: Performance Tuning

```sql
-- Check current configuration
SELECT name, setting, unit, context
FROM pg_settings
WHERE name IN (
    'shared_buffers',
    'effective_cache_size',
    'work_mem',
    'maintenance_work_mem',
    'max_connections',
    'random_page_cost',
    'effective_io_concurrency'
);

-- Analyze table statistics
ANALYZE;

-- Update table statistics
UPDATE pg_stat_user_tables SET last_analyze = NOW();
```

---

## Troubleshooting

### Common Issues and Solutions

#### 1. Connection Issues
```bash
# Test database connectivity
psql -h $RDS_ENDPOINT -U malayalees_app -d malayalees_us_site

# Check security group rules
aws ec2 describe-security-groups --group-ids sg-12345678

# Check VPC configuration
aws ec2 describe-vpcs --vpc-ids $VPC_ID
```

#### 2. Performance Issues
```sql
-- Check active connections
SELECT count(*) FROM pg_stat_activity;

-- Check lock information
SELECT
    locktype,
    database,
    relation,
    page,
    tuple,
    virtualxid,
    transactionid,
    classid,
    objid,
    objsubid,
    virtualtransaction,
    pid,
    mode,
    granted
FROM pg_locks;

-- Check slow queries
SELECT
    query,
    calls,
    total_time,
    mean_time
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 5;
```

#### 3. Cache Issues
```sql
-- Check cache table size
SELECT
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables
WHERE tablename IN ('app_cache', 'session_store');

-- Check cache hit ratio
SELECT
    heap_blks_hit,
    heap_blks_read,
    round(heap_blks_hit::numeric / (heap_blks_hit + heap_blks_read) * 100, 2) as cache_hit_ratio
FROM pg_statio_user_tables
WHERE relname = 'app_cache';

-- Manual cache cleanup
SELECT cleanup_expired_cache();
SELECT cleanup_expired_sessions();
```

#### 4. Backup and Recovery
```bash
# Create manual snapshot
aws rds create-db-snapshot \
    --db-instance-identifier malayalees-postgres \
    --db-snapshot-identifier malayalees-postgres-backup-$(date +%Y%m%d) \
    --region us-east-1

# Restore from snapshot
aws rds restore-db-instance-from-db-snapshot \
    --db-instance-identifier malayalees-postgres-restored \
    --db-snapshot-identifier malayalees-postgres-backup-20250124 \
    --region us-east-1
```

---

## Cost Optimization

### 1. Right-Sizing Resources
- Start with `db.t3.micro` for development
- Monitor usage and scale up as needed
- Use `db.t3.small` for production with moderate load
- Consider `db.t3.medium` for high-traffic applications

### 2. Storage Optimization
```bash
# Check storage usage
aws rds describe-db-instances \
    --db-instance-identifier malayalees-postgres \
    --query 'DBInstances[0].AllocatedStorage' \
    --region us-east-1

# Monitor storage metrics
aws cloudwatch get-metric-statistics \
    --namespace AWS/RDS \
    --metric-name FreeStorageSpace \
    --dimensions Name=DBInstanceIdentifier,Value=malayalees-postgres \
    --start-time 2025-01-24T00:00:00Z \
    --end-time 2025-01-24T23:59:59Z \
    --period 3600 \
    --statistics Average \
    --region us-east-1
```

### 3. Connection Pooling
```yaml
# Optimize HikariCP settings
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000
```

---

## Security Best Practices

### 1. Network Security
- Use VPC with private subnets
- Configure security groups properly
- Enable VPC Flow Logs
- Use SSL/TLS for connections

### 2. Access Control
- Use least privilege principle
- Create separate users for different purposes
- Rotate passwords regularly
- Use IAM database authentication

### 3. Encryption
- Enable encryption at rest
- Use SSL/TLS for connections
- Encrypt sensitive data in application
- Use AWS KMS for key management

---

## Deployment Checklist

### Pre-Deployment
- [ ] RDS parameter group created
- [ ] RDS subnet group created
- [ ] RDS instance created and available
- [ ] Database and user created
- [ ] Permissions granted

### Deployment
- [ ] Caching tables created
- [ ] Cache management functions created
- [ ] Materialized views created
- [ ] Automated cleanup scheduled
- [ ] Monitoring configured

### Post-Deployment
- [ ] Connection testing completed
- [ ] Performance monitoring configured
- [ ] Backup strategy implemented
- [ ] Security review completed
- [ ] Documentation updated

---

## Next Steps

1. **Run Database Migrations**: Execute Liquibase migrations
2. **Test Application**: Verify all functionality works
3. **Performance Testing**: Load test the application
4. **Monitoring Setup**: Configure comprehensive monitoring
5. **Backup Strategy**: Implement automated backups
6. **Security Review**: Conduct security assessment

---

*Last Updated: January 2025*
*Version: 1.0 - PostgreSQL Database Setup Guide*
