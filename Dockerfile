FROM openjdk:11-jdk

ARG BUILD_PATH
ADD ${BUILD_PATH} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
