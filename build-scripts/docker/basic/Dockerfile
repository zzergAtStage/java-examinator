FROM amazoncorretto:20

ARG JAR_PORT
EXPOSE ${JAR_PORT}
ARG JAR_FILE
ADD ${JAR_FILE} /opt/app.jar

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]