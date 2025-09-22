# Local Testing Guide
## Multi-Tenant Spring Boot Application

### Table of Contents
1. [Overview](#overview)
2. [Quick Start](#quick-start)
3. [Architecture Comparison](#architecture-comparison)
4. [Local Testing Environment](#local-testing-environment)
5. [Testing Multi-Tenancy](#testing-multi-tenancy)
6. [Performance Testing](#performance-testing)
7. [Troubleshooting](#troubleshooting)

---

## Overview

This guide provides comprehensive instructions for testing your multi-tenant Spring Boot application locally before deploying to AWS. The local environment includes:

✅ **PostgreSQL Database** with test data
✅ **Redis Cache** for session management  
✅ **Spring Boot Application** with local configuration
✅ **Management UIs** (pgAdmin, Redis Commander, MailHog)
✅ **Complete Test Data** for all tenants

---

## Quick Start

### Windows Users
```bash
# 1. Start the complete local environment
scripts\local-test.bat start

# 2. View logs
scripts\local-test.bat logs app

# 3. Stop services
scripts\local-test.bat stop
```

### Linux/Mac Users
```bash
# 1. Make script executable
chmod +x scripts/local-test.sh

# 2. Start the complete local environment
./scripts/local-test.sh start

# 3. View logs
./scripts/local-test.sh logs app

# 4. Stop services
./scripts/local-test.sh stop
```

---

## Architecture Comparison

### Original: EC2 + Auto Scaling
| Aspect | Rating | Notes |
|--------|--------|-------|
| **Automation** | ⭐⭐⭐ | Manual server management |
| **Cost** | ⭐⭐⭐ | $125-500/month |
| **Maintenance** | ⭐⭐ | High overhead |
| **Scaling** | ⭐⭐⭐ | Manual configuration |

### Recommended: Fargate
| Aspect | Rating | Notes |
|--------|--------|-------|
| **Automation** | ⭐⭐⭐⭐⭐ | Fully automated |
| **Cost** | ⭐⭐⭐⭐ | $95-585/month |
| **Maintenance** | ⭐⭐⭐⭐⭐ | Zero maintenance |
| **Scaling** | ⭐⭐⭐⭐⭐ | Built-in auto-scaling |

### Advanced: EKS
| Aspect | Rating | Notes |
|--------|--------|-------|
| **Automation** | ⭐⭐⭐⭐⭐ | Kubernetes orchestration |
| **Cost** | ⭐⭐⭐ | $120-600/month |
| **Maintenance** | ⭐⭐⭐⭐⭐ | Very low |
| **Scaling** | ⭐⭐⭐⭐⭐ | Advanced orchestration |

---

## Local Testing Environment

### Services Included

#### 1. PostgreSQL Database
- **Port**: 5432
- **Database**: malayalees_us_site
- **User**: postgres
- **Password**: password
- **Features**: Complete schema with test data

#### 2. Redis Cache
- **Port**: 6379
- **Features**: Session storage, caching
- **Memory**: 256MB limit

#### 3. Spring Boot Application
- **Port**: 8080
- **Profile**: local
- **Features**: Enhanced logging, relaxed security

#### 4. Management UIs
- **pgAdmin**: http://localhost:5050 (admin@local.com/admin)
- **Redis Commander**: http://localhost:8081 (admin/admin)
- **MailHog**: http://localhost:8025

### Test Data Structure

```
3 Tenants:
├── tenant_demo_001 (demo1.local)
│   ├── 3 Users (admin, user1, user2)
│   ├── 2 Events (Spring Festival, Tech Meetup)
│   ├── 2 Polls (Favorite Food, Event Timing)
│   └── 4 Poll Options + 4 Responses
├── tenant_demo_002 (demo2.local)
│   ├── 3 Users (admin, user1, user2)
│   ├── 2 Events (Cultural Night, Business Conference)
│   ├── 2 Polls (Music Preference, Conference Topics)
│   └── 8 Poll Options + 4 Responses
└── tenant_demo_003 (demo3.local)
    ├── 3 Users (admin, user1, user2)
    ├── 2 Events (Food Festival, Sports Tournament)
    ├── 2 Polls (Dish Rating, Team Preference)
    └── 8 Poll Options + 4 Responses
```

---

## Testing Multi-Tenancy

### 1. Tenant Isolation Testing

#### Test Tenant Data Access
```bash
# Test tenant 1 data
curl -H "X-Tenant-ID: tenant_demo_001" \
     http://localhost:8080/api/events

# Test tenant 2 data  
curl -H "X-Tenant-ID: tenant_demo_002" \
     http://localhost:8080/api/events

# Test cross-tenant access (should be blocked)
curl -H "X-Tenant-ID: tenant_demo_001" \
     http://localhost:8080/api/events?tenantId=tenant_demo_002
```

#### Test Poll Responses
```bash
# Submit response for tenant 1
curl -X POST http://localhost:8080/api/poll-responses \
     -H "Content-Type: application/json" \
     -H "X-Tenant-ID: tenant_demo_001" \
     -d '{"pollId": 1, "pollOptionId": 1, "responseValue": "Biryani"}'

# Submit response for tenant 2
curl -X POST http://localhost:8080/api/poll-responses \
     -H "Content-Type: application/json" \
     -H "X-Tenant-ID: tenant_demo_002" \
     -d '{"pollId": 3, "pollOptionId": 8, "responseValue": "Classical"}'
```

### 2. Database Connection Testing

#### Connection Pool Testing
```bash
# Check database connections
curl http://localhost:8080/management/health/db

# Check connection pool metrics
curl http://localhost:8080/management/metrics/hikaricp.connections
```

#### Redis Connection Testing
```bash
# Check Redis connectivity
curl http://localhost:8080/management/health/redis

# Test session storage
curl -c cookies.txt http://localhost:8080/api/auth/login
curl -b cookies.txt http://localhost:8080/api/user/profile
```

### 3. Performance Testing

#### Load Testing with Apache Bench
```bash
# Test API endpoints
ab -n 1000 -c 10 http://localhost:8080/api/events

# Test with tenant header
ab -n 1000 -c 10 -H "X-Tenant-ID: tenant_demo_001" \
   http://localhost:8080/api/events
```

#### Database Performance Testing
```sql
-- Test query performance in pgAdmin
EXPLAIN ANALYZE 
SELECT * FROM event_details 
WHERE tenant_id = 'tenant_demo_001';

-- Test index usage
EXPLAIN ANALYZE 
SELECT * FROM event_poll_response 
WHERE tenant_id = 'tenant_demo_001' 
  AND poll_id = 1;
```

---

## Performance Testing

### 1. Connection Pool Testing

#### Test HikariCP Configuration
```yaml
# Monitor connection pool in application-local.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 5
      minimum-idle: 1
      leak-detection-threshold: 60000
```

#### Connection Pool Metrics
```bash
# Check active connections
curl http://localhost:8080/management/metrics/hikaricp.connections.active

# Check idle connections  
curl http://localhost:8080/management/metrics/hikaricp.connections.idle

# Check connection timeouts
curl http://localhost:8080/management/metrics/hikaricp.connections.timeout
```

### 2. Cache Performance Testing

#### Redis Cache Testing
```bash
# Test cache hit rate
curl http://localhost:8080/management/metrics/cache.gets

# Test cache evictions
curl http://localhost:8080/management/metrics/cache.evictions

# Test Redis memory usage
docker-compose -f docker-compose.local.yml exec redis redis-cli info memory
```

### 3. Multi-Tenant Performance

#### Concurrent Tenant Testing
```bash
# Test multiple tenants simultaneously
for tenant in tenant_demo_001 tenant_demo_002 tenant_demo_003; do
  ab -n 100 -c 5 -H "X-Tenant-ID: $tenant" \
     http://localhost:8080/api/events &
done
wait
```

---

## Troubleshooting

### Common Issues

#### 1. Database Connection Issues
```bash
# Check PostgreSQL status
docker-compose -f docker-compose.local.yml logs postgres

# Test database connectivity
docker-compose -f docker-compose.local.yml exec postgres pg_isready -U postgres

# Check database logs
docker-compose -f docker-compose.local.yml exec postgres psql -U postgres -c "SELECT * FROM pg_stat_activity;"
```

#### 2. Redis Connection Issues
```bash
# Check Redis status
docker-compose -f docker-compose.local.yml logs redis

# Test Redis connectivity
docker-compose -f docker-compose.local.yml exec redis redis-cli ping

# Check Redis memory
docker-compose -f docker-compose.local.yml exec redis redis-cli info memory
```

#### 3. Application Issues
```bash
# Check application logs
docker-compose -f docker-compose.local.yml logs app

# Check application health
curl http://localhost:8080/management/health

# Check application metrics
curl http://localhost:8080/management/metrics
```

### Performance Issues

#### 1. Slow Database Queries
```sql
-- Enable query logging in PostgreSQL
ALTER SYSTEM SET log_statement = 'all';
ALTER SYSTEM SET log_min_duration_statement = 1000;
SELECT pg_reload_conf();
```

#### 2. High Memory Usage
```bash
# Check JVM memory usage
curl http://localhost:8080/management/metrics/jvm.memory.used

# Check Redis memory usage
docker-compose -f docker-compose.local.yml exec redis redis-cli info memory | grep used_memory_human
```

#### 3. Connection Pool Exhaustion
```bash
# Monitor connection pool
curl http://localhost:8080/management/metrics/hikaricp.connections.pending

# Check for connection leaks
curl http://localhost:8080/management/metrics/hikaricp.connections.leak
```

### Reset Environment

#### Complete Reset
```bash
# Stop and remove all containers and volumes
docker-compose -f docker-compose.local.yml down -v

# Remove unused images
docker image prune -f

# Restart from scratch
./scripts/local-test.sh start
```

#### Database Reset Only
```bash
# Reset database only
docker-compose -f docker-compose.local.yml stop postgres
docker-compose -f docker-compose.local.yml rm -f postgres
docker volume rm malayalees-us-site-boot_postgres_data

# Restart database
docker-compose -f docker-compose.local.yml up -d postgres
```

---

## Next Steps

### 1. Local Testing Complete
After successful local testing:
- ✅ All APIs working correctly
- ✅ Multi-tenant isolation verified
- ✅ Database performance acceptable
- ✅ Redis caching working
- ✅ Connection pooling optimized

### 2. Deploy to AWS Fargate
```bash
# Deploy to AWS Fargate (recommended)
./src/main/resources/documentation/deployment-scripts/deploy-aws.sh deploy

# Or deploy to EKS (advanced)
kubectl apply -f k8s/
```

### 3. Production Monitoring
- Set up CloudWatch alarms
- Configure auto-scaling policies
- Monitor database performance
- Track tenant usage metrics

---

## Summary

The local testing environment provides:

✅ **Complete Multi-Tenant Testing**: 3 test tenants with full data
✅ **Database Performance Testing**: PostgreSQL with optimized settings
✅ **Cache Testing**: Redis with session management
✅ **Connection Pool Testing**: HikariCP with monitoring
✅ **Management UIs**: pgAdmin, Redis Commander, MailHog
✅ **Automated Scripts**: One-command setup and testing

This setup allows you to thoroughly test your application locally before deploying to AWS, ensuring a smooth production deployment.

---

*Last Updated: January 2025*
*Version: 1.0*
