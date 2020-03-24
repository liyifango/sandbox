FROM openjdk:11-jre-slim

ARG BUILD_PATH
ADD ${BUILD_PATH} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
