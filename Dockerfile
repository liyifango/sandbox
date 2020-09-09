FROM openjdk:11-jdk

ARG BUILD_PATH
ADD ${BUILD_PATH} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9999","-jar","/app.jar"]
