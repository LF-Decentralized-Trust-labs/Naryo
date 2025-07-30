FROM openjdk:21-jdk-slim

ARG JAVA_PORT
ENV JAVA_PORT=${JAVA_PORT:-8086}

COPY target/naryo-server.jar /app/naryo-server.jar

CMD ["bash", "-c", "java -Dserver.port=${JAVA_PORT} -jar /app/naryo-server.jar"]
