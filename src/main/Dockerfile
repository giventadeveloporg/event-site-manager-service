FROM openjdk:17-jre-slim

# Copy JAR file
COPY target/nextjs-template-boot-0.0.1-SNAPSHOT.jar /app.jar

# Expose port (will be set via environment variables)
EXPOSE 80

# Health check (will use port from SERVER_PORT env var)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT:-80}/management/health || exit 1

# Run Spring Boot - DO NOT use CMD, let environment variables control configuration
ENTRYPOINT ["java", "-jar", "/app.jar"]
