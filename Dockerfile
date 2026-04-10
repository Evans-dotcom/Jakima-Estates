# Use Java 17 (important for Spring Boot)
FROM eclipse-temurin:21-jdk-alpine

# Create app directory
WORKDIR /app

# Copy jar file
COPY target/*.jar app.jar

# Expose port (Render uses dynamic PORT)
EXPOSE 8080

# Run app
ENTRYPOINT ["java","-jar","app.jar"]