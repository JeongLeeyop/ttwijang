FROM openjdk:17-oracle
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN echo Asia/Seoul > /etc/timezone
VOLUME /tmp
WORKDIR api-server
ARG JAR_FILE=target/api-server.jar
COPY ${JAR_FILE} api-server.jar
ENTRYPOINT ["java","-jar","api-server.jar"]