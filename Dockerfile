FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

RUN apt-get update \
 && apt-get install -y ca-certificates \
 && update-ca-certificates \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /app/target/kulakwetu-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java",
 "-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts",
 "-Djavax.net.ssl.trustStorePassword=changeit",
 "-jar","app.jar"]