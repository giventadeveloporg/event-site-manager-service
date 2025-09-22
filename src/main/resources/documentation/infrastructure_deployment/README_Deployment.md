# Deployment Quick Start Guide

## Overview
This guide provides step-by-step instructions for deploying the multi-tenant Spring Boot application to AWS with minimal configuration changes.

## Prerequisites
- AWS CLI configured
- Docker installed
- Node.js (for CDK)
- Maven

## Quick Deployment

### 1. Clone and Setup
```bash
git clone <repository>
cd malayalees-us-site-boot
chmod +x src/main/resources/documentation/deployment-scripts/deploy-aws.sh
```

### 2. Configure Environment
```bash
export AWS_REGION=us-east-1
export APPLICATION_NAME=malayalees-us-site-boot
export ENVIRONMENT=prod
```

### 3. Deploy Infrastructure
```bash
cd src/main/resources/documentation/infrastructure
npm install
npx cdk bootstrap
npx cdk deploy
```

### 4. Deploy Application
```bash
./src/main/resources/documentation/deployment-scripts/deploy-aws.sh deploy
```

## Cost Estimation
- **Initial (10 domains, 1K users)**: ~$125/month
- **Growth (50 domains, 10K users)**: ~$200/month
- **Scale (100+ domains, 100K+ users)**: ~$300-500/month

## Architecture Benefits
✅ **Auto-scaling**: Handles traffic spikes automatically
✅ **High Availability**: Multi-AZ deployment
✅ **Cost Optimized**: Pay only for what you use
✅ **Secure**: VPC isolation and encryption
✅ **Monitoring**: CloudWatch integration
✅ **Zero Downtime**: Blue-green deployments

## Support
For detailed documentation, see `AWS_Deployment_Architecture_Guide.md`
