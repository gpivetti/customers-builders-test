FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine-slim as app
RUN rm -f /etc/localtime && ln -s /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime
VOLUME /tmp
EXPOSE 8080
COPY target/*.jar /app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]