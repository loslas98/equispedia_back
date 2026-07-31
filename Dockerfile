# --- Stage 1: Build ---
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml first (better layer caching -
# dependencies only re-download if pom.xml changes, not on every code change)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer unless pom.xml changes)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Now copy the actual source code
COPY src src

# Build the jar, skipping tests (run tests separately in CI if needed)
RUN ./mvnw clean package -DskipTests -B

# --- Stage 2: Run ---
FROM eclipse-temurin:21-jre AS run

WORKDIR /app

# Copy only the built jar from the build stage - keeps final image small
COPY --from=build /app/target/*.jar app.jar

# Match the port your app runs on (from application.properties: server.port=8081)
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]