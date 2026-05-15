FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

#ai start
COPY .mvn/ .mvn/
COPY mvnw .
#ai end

COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]