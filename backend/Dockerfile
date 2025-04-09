# Etapa de build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/projetoloja-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
