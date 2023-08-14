FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/gm-demo-0.0.1-SNAPSHOT.jar /app/demoapp.jar
ENTRYPOINT java -Xms256m -Xmx1024m -jar /app/demoapp.jar