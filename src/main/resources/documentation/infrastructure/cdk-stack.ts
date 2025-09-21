// ===================================================================
// AWS CDK Stack for Multi-Tenant Spring Boot Application
// ===================================================================
// This CDK stack creates all necessary AWS infrastructure for
// deploying the Spring Boot application with auto-scaling and monitoring.
// ===================================================================

import * as cdk from '@aws-cdk/core';
import * as ec2 from '@aws-cdk/aws-ec2';
import * as ecs from '@aws-cdk/aws-ecs';
import * as ecsPatterns from '@aws-cdk/aws-ecs-patterns';
import * as rds from '@aws-cdk/aws-rds';
import * as elasticache from '@aws-cdk/aws-elasticache';
import * as elbv2 from '@aws-cdk/aws-elasticloadbalancingv2';
import * as cloudwatch from '@aws-cdk/aws-cloudwatch';
import * as iam from '@aws-cdk/aws-iam';
import * as logs from '@aws-cdk/aws-logs';
import * as s3 from '@aws-cdk/aws-s3';
import * as ssm from '@aws-cdk/aws-ssm';

export interface SpringBootAppStackProps extends cdk.StackProps {
  applicationName: string;
  environment: string;
  domainName?: string;
}

export class SpringBootAppStack extends cdk.Stack {
  public readonly vpc: ec2.Vpc;
  public readonly database: rds.DatabaseInstance;
  public readonly redis: elasticache.CfnCacheCluster;
  public readonly cluster: ecs.Cluster;
  public readonly service: ecsPatterns.ApplicationLoadBalancedFargateService;
  public readonly bucket: s3.Bucket;

  constructor(scope: cdk.Construct, id: string, props: SpringBootAppStackProps) {
    super(scope, id, props);

    const { applicationName, environment } = props;

    // ===================================================================
    // VPC Configuration
    // ===================================================================
    this.vpc = new ec2.Vpc(this, 'AppVPC', {
      maxAzs: 2,
      natGateways: 1, // Cost optimization
      subnetConfiguration: [
        {
          cidrMask: 24,
          name: 'Public',
          subnetType: ec2.SubnetType.PUBLIC,
        },
        {
          cidrMask: 24,
          name: 'Private',
          subnetType: ec2.SubnetType.PRIVATE_WITH_NAT,
        },
        {
          cidrMask: 24,
          name: 'Database',
          subnetType: ec2.SubnetType.PRIVATE_ISOLATED,
        },
      ],
    });

    // ===================================================================
    // Security Groups
    // ===================================================================
    const albSecurityGroup = new ec2.SecurityGroup(this, 'ALBSecurityGroup', {
      vpc: this.vpc,
      description: 'Security group for Application Load Balancer',
    });
    albSecurityGroup.addIngressRule(ec2.Peer.anyIpv4(), ec2.Port.tcp(80), 'HTTP');
    albSecurityGroup.addIngressRule(ec2.Peer.anyIpv4(), ec2.Port.tcp(443), 'HTTPS');

    const appSecurityGroup = new ec2.SecurityGroup(this, 'AppSecurityGroup', {
      vpc: this.vpc,
      description: 'Security group for Spring Boot application',
    });
    appSecurityGroup.addIngressRule(albSecurityGroup, ec2.Port.tcp(8080), 'From ALB');

    const dbSecurityGroup = new ec2.SecurityGroup(this, 'DBSecurityGroup', {
      vpc: this.vpc,
      description: 'Security group for RDS database',
    });
    dbSecurityGroup.addIngressRule(appSecurityGroup, ec2.Port.tcp(5432), 'From application');

    const redisSecurityGroup = new ec2.SecurityGroup(this, 'RedisSecurityGroup', {
      vpc: this.vpc,
      description: 'Security group for Redis cache',
    });
    redisSecurityGroup.addIngressRule(appSecurityGroup, ec2.Port.tcp(6379), 'From application');

    // ===================================================================
    // S3 Bucket for file storage
    // ===================================================================
    this.bucket = new s3.Bucket(this, 'AppStorage', {
      bucketName: `${applicationName}-${environment}-storage`,
      versioned: true,
      encryption: s3.BucketEncryption.S3_MANAGED,
      blockPublicAccess: s3.BlockPublicAccess.BLOCK_ALL,
      lifecycleRules: [
        {
          id: 'DeleteOldVersions',
          enabled: true,
          noncurrentVersionExpiration: cdk.Duration.days(30),
        },
      ],
    });

    // ===================================================================
    // RDS PostgreSQL Database
    // ===================================================================
    this.database = new rds.DatabaseInstance(this, 'AppDatabase', {
      engine: rds.DatabaseInstanceEngine.postgres({
        version: rds.PostgresEngineVersion.VER_13_7,
      }),
      instanceType: ec2.InstanceType.of(ec2.InstanceClass.T3, ec2.InstanceSize.MICRO),
      vpc: this.vpc,
      vpcSubnets: {
        subnetType: ec2.SubnetType.PRIVATE_ISOLATED,
      },
      securityGroups: [dbSecurityGroup],
      multiAz: true,
      backupRetention: cdk.Duration.days(7),
      deleteAutomatedBackups: false,
      deletionProtection: false, // Set to true for production
      databaseName: 'malayalees_us_site',
      credentials: rds.Credentials.fromGeneratedSecret('dbadmin'),
      storageEncrypted: true,
      monitoringInterval: cdk.Duration.seconds(60),
      enablePerformanceInsights: true,
      performanceInsightRetention: rds.PerformanceInsightRetention.DEFAULT,
      parameterGroup: new rds.ParameterGroup(this, 'DBParameterGroup', {
        engine: rds.DatabaseInstanceEngine.postgres({
          version: rds.PostgresEngineVersion.VER_13_7,
        }),
        parameters: {
          shared_preload_libraries: 'pg_stat_statements',
          log_statement: 'all',
          log_min_duration_statement: '1000',
          max_connections: '100',
        },
      }),
    });

    // ===================================================================
    // ElastiCache Redis
    // ===================================================================
    const redisSubnetGroup = new elasticache.CfnSubnetGroup(this, 'RedisSubnetGroup', {
      description: 'Subnet group for Redis cache',
      subnetIds: this.vpc.privateSubnets.map(subnet => subnet.subnetId),
    });

    this.redis = new elasticache.CfnCacheCluster(this, 'AppRedis', {
      cacheNodeType: 'cache.t3.micro',
      engine: 'redis',
      numCacheNodes: 1,
      vpcSecurityGroupIds: [redisSecurityGroup.securityGroupId],
      cacheSubnetGroupName: redisSubnetGroup.ref,
      port: 6379,
      parameterGroupName: 'default.redis6.x',
    });

    // ===================================================================
    // ECS Cluster
    // ===================================================================
    this.cluster = new ecs.Cluster(this, 'AppCluster', {
      vpc: this.vpc,
      clusterName: `${applicationName}-cluster`,
      containerInsights: true,
    });

    // ===================================================================
    // ECS Task Definition
    // ===================================================================
    const taskDefinition = new ecs.FargateTaskDefinition(this, 'AppTaskDefinition', {
      memoryLimitMiB: 1024,
      cpu: 512,
      family: `${applicationName}-task`,
    });

    // Add CloudWatch Log Group
    const logGroup = new logs.LogGroup(this, 'AppLogGroup', {
      logGroupName: `/ecs/${applicationName}`,
      retention: logs.RetentionDays.ONE_MONTH,
    });

    // ===================================================================
    // ECS Container Definition
    // ===================================================================
    const container = taskDefinition.addContainer('AppContainer', {
      image: ecs.ContainerImage.fromRegistry(`${this.account}.dkr.ecr.${this.region}.amazonaws.com/${applicationName}:latest`),
      logging: ecs.LogDrivers.awsLogs({
        streamPrefix: 'ecs',
        logGroup,
      }),
      environment: {
        SPRING_PROFILES_ACTIVE: 'prod-aws',
        RDS_ENDPOINT: this.database.instanceEndpoint.hostname,
        DB_NAME: 'malayalees_us_site',
        REDIS_ENDPOINT: this.redis.attrRedisEndpointAddress,
        AWS_REGION: this.region,
        S3_BUCKET_NAME: this.bucket.bucketName,
      },
      secrets: {
        DB_USERNAME: ecs.Secret.fromSecretsManager(this.database.secret!, 'username'),
        DB_PASSWORD: ecs.Secret.fromSecretsManager(this.database.secret!, 'password'),
      },
      portMappings: [
        {
          containerPort: 8080,
          protocol: ecs.Protocol.TCP,
        },
      ],
      healthCheck: {
        command: ['CMD-SHELL', 'curl -f http://localhost:8080/management/health || exit 1'],
        interval: cdk.Duration.seconds(30),
        timeout: cdk.Duration.seconds(5),
        retries: 3,
        startPeriod: cdk.Duration.seconds(60),
      },
    });

    // ===================================================================
    // ECS Service with Application Load Balancer
    // ===================================================================
    this.service = new ecsPatterns.ApplicationLoadBalancedFargateService(this, 'AppService', {
      cluster: this.cluster,
      serviceName: `${applicationName}-service`,
      taskDefinition,
      desiredCount: 2,
      publicLoadBalancer: true,
      loadBalancerName: `${applicationName}-alb`,
      listenerPort: 80,
      healthCheckGracePeriod: cdk.Duration.seconds(300),
      targetProtocol: elbv2.ApplicationProtocol.HTTP,
      protocol: elbv2.ApplicationProtocol.HTTP,
      securityGroups: [appSecurityGroup],
      assignPublicIp: false,
    });

    // Configure auto scaling
    const scalableTarget = this.service.service.autoScaleTaskCount({
      minCapacity: 2,
      maxCapacity: 10,
    });

    scalableTarget.scaleOnCpuUtilization('CpuScaling', {
      targetUtilizationPercent: 70,
      scaleInCooldown: cdk.Duration.seconds(300),
      scaleOutCooldown: cdk.Duration.seconds(300),
    });

    scalableTarget.scaleOnMemoryUtilization('MemoryScaling', {
      targetUtilizationPercent: 80,
      scaleInCooldown: cdk.Duration.seconds(300),
      scaleOutCooldown: cdk.Duration.seconds(300),
    });

    // Add custom metric scaling
    scalableTarget.scaleOnMetric('RequestCountScaling', {
      metric: new cloudwatch.Metric({
        namespace: 'AWS/ApplicationELB',
        metricName: 'RequestCountPerTarget',
        dimensionsMap: {
          TargetGroup: this.service.targetGroup.targetGroupFullName,
        },
      }),
      scalingSteps: [
        { upper: 1000, change: -1 },
        { lower: 1000, upper: 2000, change: 0 },
        { lower: 2000, change: +1 },
      ],
      adjustmentType: cloudwatch.AdjustmentType.CHANGE_IN_CAPACITY,
    });

    // ===================================================================
    // IAM Role for ECS Task
    // ===================================================================
    const taskRole = new iam.Role(this, 'AppTaskRole', {
      assumedBy: new iam.ServicePrincipal('ecs-tasks.amazonaws.com'),
      inlinePolicies: {
        S3Access: new iam.PolicyDocument({
          statements: [
            new iam.PolicyStatement({
              effect: iam.Effect.ALLOW,
              actions: ['s3:GetObject', 's3:PutObject', 's3:DeleteObject'],
              resources: [this.bucket.bucketArn + '/*'],
            }),
            new iam.PolicyStatement({
              effect: iam.Effect.ALLOW,
              actions: ['s3:ListBucket'],
              resources: [this.bucket.bucketArn],
            }),
          ],
        }),
        CloudWatchAccess: new iam.PolicyDocument({
          statements: [
            new iam.PolicyStatement({
              effect: iam.Effect.ALLOW,
              actions: [
                'cloudwatch:PutMetricData',
                'logs:CreateLogGroup',
                'logs:CreateLogStream',
                'logs:PutLogEvents',
              ],
              resources: ['*'],
            }),
          ],
        }),
      },
    });

    taskDefinition.addToTaskRolePolicy(
      new iam.PolicyStatement({
        effect: iam.Effect.ALLOW,
        actions: ['secretsmanager:GetSecretValue'],
        resources: [this.database.secret!.secretArn],
      })
    );

    // ===================================================================
    // CloudWatch Alarms
    // ===================================================================
    new cloudwatch.Alarm(this, 'HighCPUAlarm', {
      metric: this.service.service.metricCpuUtilization(),
      threshold: 80,
      evaluationPeriods: 2,
      treatMissingData: cloudwatch.TreatMissingData.BREACHING,
    });

    new cloudwatch.Alarm(this, 'HighMemoryAlarm', {
      metric: this.service.service.metricMemoryUtilization(),
      threshold: 80,
      evaluationPeriods: 2,
      treatMissingData: cloudwatch.TreatMissingData.BREACHING,
    });

    new cloudwatch.Alarm(this, 'HighRequestCountAlarm', {
      metric: new cloudwatch.Metric({
        namespace: 'AWS/ApplicationELB',
        metricName: 'RequestCount',
        dimensionsMap: {
          LoadBalancer: this.service.loadBalancer.loadBalancerFullName,
        },
      }),
      threshold: 10000,
      evaluationPeriods: 1,
      treatMissingData: cloudwatch.TreatMissingData.NOT_BREACHING,
    });

    // ===================================================================
    // Parameter Store for configuration
    // ===================================================================
    new ssm.StringParameter(this, 'DatabaseEndpoint', {
      parameterName: `/${applicationName}/${environment}/database/endpoint`,
      stringValue: this.database.instanceEndpoint.hostname,
      description: 'RDS endpoint for the application',
    });

    new ssm.StringParameter(this, 'RedisEndpoint', {
      parameterName: `/${applicationName}/${environment}/redis/endpoint`,
      stringValue: this.redis.attrRedisEndpointAddress,
      description: 'Redis endpoint for the application',
    });

    new ssm.StringParameter(this, 'S3BucketName', {
      parameterName: `/${applicationName}/${environment}/s3/bucket`,
      stringValue: this.bucket.bucketName,
      description: 'S3 bucket name for file storage',
    });

    // ===================================================================
    // Outputs
    // ===================================================================
    new cdk.CfnOutput(this, 'LoadBalancerDNS', {
      value: this.service.loadBalancer.loadBalancerDnsName,
      description: 'Application Load Balancer DNS name',
    });

    new cdk.CfnOutput(this, 'DatabaseEndpoint', {
      value: this.database.instanceEndpoint.hostname,
      description: 'RDS database endpoint',
    });

    new cdk.CfnOutput(this, 'RedisEndpoint', {
      value: this.redis.attrRedisEndpointAddress,
      description: 'Redis cache endpoint',
    });

    new cdk.CfnOutput(this, 'S3BucketName', {
      value: this.bucket.bucketName,
      description: 'S3 bucket for file storage',
    });

    new cdk.CfnOutput(this, 'ClusterName', {
      value: this.cluster.clusterName,
      description: 'ECS cluster name',
    });

    new cdk.CfnOutput(this, 'ServiceName', {
      value: this.service.service.serviceName,
      description: 'ECS service name',
    });
  }
}
