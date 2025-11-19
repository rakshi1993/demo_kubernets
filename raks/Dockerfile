# ============================
#   Stage 1 — Build the JAR
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy only pom.xml first (to cache dependencies)
COPY pom.xml .

# Download dependencies (cached)
RUN mvn dependency:go-offline -B

# Copy actual source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests


# ============================
#   Stage 2 — Create Runtime Image
# ============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy only the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
