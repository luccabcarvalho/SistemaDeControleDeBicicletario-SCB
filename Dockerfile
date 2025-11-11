# ----- ESTÁGIO 1: Build (Compilação) -----
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY aluguel-ms/pom.xml ./pom.xml

RUN mvn -B dependency:go-offline

COPY aluguel-ms/src ./src

RUN mvn -B clean package -DskipTests

# ----- ESTÁGIO 2: Run (Execução) -----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /build/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]