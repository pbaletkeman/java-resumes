# Multi-stage build for Java Spring Boot Backend

# Stage 1: Build the application
# Using Eclipse Temurin JDK 21 for build (fully Gradle compatible)
# Java 21 bytecode runs fine on Java 25 runtime
FROM eclipse-temurin:21-jdk-alpine AS builder

# Build argument to invalidate cache when needed
ARG CACHE_BUSTER=1

WORKDIR /app

# Install Gradle 8.10 manually
RUN apk add --no-cache curl unzip && \
    curl -L https://services.gradle.org/distributions/gradle-8.10-bin.zip -o gradle.zip && \
    unzip -q gradle.zip && \
    rm gradle.zip && \
    mv gradle-8.10 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle && \
    rm -rf /tmp/* && \
    apk del curl unzip

# Set Gradle home for reference
ENV GRADLE_HOME=/opt/gradle

# Copy gradle files
COPY build.gradle settings.gradle gradle.properties ./

# Copy source code
COPY src/ src/

# Copy configuration files
COPY config/ config/
COPY config.json ./

# Build the application (skip tests for faster builds)
RUN gradle build -x test --no-daemon

# Stage 2: Runtime
# Using Eclipse Temurin JDK 21 for runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Copy JAR from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Copy configuration
COPY --from=builder /app/config.json ./

# Create necessary directories (files for uploads, data for SQLite database)
RUN mkdir -p /app/files /app/data && chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# JVM options for containers
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
