FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY main-service/pom.xml main-service/
COPY dwh-service/pom.xml dwh-service/

COPY main-service/src main-service/src

RUN mvn clean package -DskipTests -pl main-service -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/main-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]