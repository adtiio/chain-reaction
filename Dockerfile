# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application source code into the container
COPY . .

# Add the system.properties to ensure the correct JDK version is used
RUN echo "java.runtime.version=17" > system.properties

# Build the project using Maven
RUN ./mvnw clean package -DskipTests

# Expose the default port for the Spring Boot application
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/chain-reaction-0.0.1-SNAPSHOT.jar"]
