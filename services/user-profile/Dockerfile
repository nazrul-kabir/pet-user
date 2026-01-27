# Use Maven image for building the application
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (leveraging Docker layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a smaller runtime image
FROM eclipse-temurin:17-jre-alpine

# Install curl for health checks and clean apk cache
RUN apk add --no-cache curl && \
    rm -rf /var/cache/apk/*

# Set working directory
WORKDIR /app

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/user-profile-*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port (default to 8080 but we'll map to 8081)
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

# Use built-in Java health check instead of curl (more reliable)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD java -jar app.jar --management.health.probes.enabled=true || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]