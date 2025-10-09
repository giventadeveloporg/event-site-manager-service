FROM eclipse-temurin:17-jre

# Copy JAR file
COPY target/nextjs-template-boot-0.0.1-SNAPSHOT.jar /app.jar

# Expose port 80 (ALB expects this)
EXPOSE 80

# Run the application on port 80
# Configuration is provided via environment variables from ECS task definition
ENTRYPOINT ["java", "-jar", "/app.jar"]