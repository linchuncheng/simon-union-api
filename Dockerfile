FROM joepjoosten/openjdk-alpine-bash
ENV TZ=Asia/Shanghai
COPY target/union-api-0.0.1.jar /app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar --server.port=8080