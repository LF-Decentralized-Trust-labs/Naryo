FROM eclipse-temurin:21

ARG JAVA_MEM_XMS
ENV JAVA_MEM_XMS=${JAVA_MEM_XMS:-512m}

ARG JAVA_MEM_XMX
ENV JAVA_MEM_XMX=${JAVA_MEM_XMX:-512m}

ARG JAVA_MEM_META
ENV JAVA_MEM_META=${JAVA_MEM_META:-256m}

ARG JAVA_EXTRA_OPTS
ENV JAVA_EXTRA_OPTS=${JAVA_EXTRA_OPTS:-}

ARG JAVA_PORT
ENV JAVA_PORT=${JAVA_PORT:-6060}

WORKDIR /app

COPY naryo-server/build/libs/naryo-server-0.0.1-SNAPSHOT.jar naryo-server.jar

CMD ["bash", "-c", "java -Dserver.port=${JAVA_PORT} -Xms${JAVA_MEM_XMS} -Xmx${JAVA_MEM_XMX} -Djava.security.egd=file:/dev/./urandom -XX:MaxMetaspaceSize=${JAVA_MEM_META} ${JAVA_EXTRA_OPTS} -jar naryo-server.jar"]

EXPOSE ${JAVA_PORT}