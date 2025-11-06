# ================================
# Stage 1: Build the Spring Boot app
# ================================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the JAR (skip tests for faster image build)
RUN mvn clean package -DskipTests

# ================================
# Stage 2: Run the built application
# ================================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Use Java 21 optimized flags (optional)
ENTRYPOINT ["java", "-XX:+UseZGC", "-jar", "app.jar"]
