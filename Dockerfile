# 第一阶段使用 layertools 的 extract 命令将应用程序拆分为多个层  本次构建标记为builder
FROM openjdk:11-slim as builder

WORKDIR application
ARG BUILD_PATH
ADD ${BUILD_PATH} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

#  第二阶段从分层中复制并构建镜像
FROM openjdk:11-slim

WORKDIR application
# 生成一个用于存储日志的目录
RUN mkdir logs
# 定义用户runner并使用runner启动容器，避免直接使用root身份启动造成安全隐患
RUN addgroup --system app && adduser --system runner --group app
USER runner
# 从上面构建的builder 中复制层  注意保证层的顺序
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]
