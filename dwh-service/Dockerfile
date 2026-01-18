FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY dwh-service/pom.xml dwh-service/
COPY main-service/pom.xml main-service/
COPY dwh-service/src dwh-service/src
RUN mvn clean package -DskipTests -pl dwh-service -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/dwh-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
