FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

ENV ACTIVE_PROFILE=test

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]