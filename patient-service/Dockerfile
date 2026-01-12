# ---- build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

# Use the image's mvn (mvnw isn't available inside the build image)
RUN mvn -B dependency:go-offline

COPY src ./src

# Build the application (skip tests for faster build in container)
RUN mvn -B package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# Copy the built jar from the builder stage (absolute path from builder's WORKDIR)
COPY --from=builder /app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "app.jar"]
