FROM  openjdk:17-jdk-slim
LABEL authors="father"
LABEL name="Jexam"

WORKDIR /app
COPY ../../target/java-examinator-0.0.1-SNAPSHOT.jar /app/java-examinator.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/java-examinator.jar"]