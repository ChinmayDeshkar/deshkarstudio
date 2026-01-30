# ======================================================
# 🧱 Stage 1: Build the Spring Boot JAR
# ======================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (for layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the project files
COPY src ./src

# Build the application JAR (skip tests for faster build)
RUN mvn clean package -DskipTests

# ======================================================
# 🚀 Stage 2: Run the Spring Boot app
# ======================================================
FROM eclipse-temurin:17-jdk-jammy

# Create app directory inside container
WORKDIR /app

# Copy built jar from builder image
COPY --from=builder /app/target/*.jar app.jar

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
# ENV PORT=8080

# Expose the port that Spring Boot runs on
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
