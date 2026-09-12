FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
# Copy built jar into container
COPY target/courseapp.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]