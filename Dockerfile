#### DOCKERFILE FOR CLOUD DEPLOY (PRD) ####

FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine-slim

RUN rm -f /etc/localtime && ln -s /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime

VOLUME /tmp

COPY target/*.jar /app.jar

CMD java -Xmx$JVM_MEMORY -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.config.location=classpath:/$PROPERTIES_SPRING

