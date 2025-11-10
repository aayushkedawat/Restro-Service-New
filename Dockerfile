# Dockerfile
# Use an official OpenJDK image with the correct version
FROM eclipse-temurin:17-jdk

# Define ARG for the JAR name
ARG JAR_FILE=target/restaurant-menu-service-0.0.1-SNAPSHOT.jar

# Copy your application JAR into the container
COPY ${JAR_FILE} /app/app.jar

# Expose the port your app runs on
EXPOSE 8085

# Command to run your app
CMD ["java", "-jar", "/app/app.jar"]
