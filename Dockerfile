FROM eclipse-temurin:17-jre

# Copy JAR file
COPY target/nextjs-template-boot-0.0.1-SNAPSHOT.jar /app.jar

# Expose the application port.
# The ECS task definition must set SERVER_PORT (e.g. 80) and SPRING_PROFILES_ACTIVE
# to match the ALB target group port and the desired Spring profile.
EXPOSE 80

# Health check via Spring Boot Actuator liveness probe.
# Uses the same SERVER_PORT that the application binds to (default 8080 if not overridden).
# The ECS container health check and ALB target group health check are the authoritative
# signals for ECS service stability — this Docker HEALTHCHECK provides a secondary indicator.
HEALTHCHECK --interval=30s --timeout=10s --start-period=120s --retries=5 \
  CMD wget -q --spider "http://localhost:${SERVER_PORT:-8080}/management/health/liveness" || exit 1

ENTRYPOINT ["java", "-jar", "/app.jar"]