FROM eclipse-temurin:21-jdk-alpine

WORKDIR /srv/cookiebot

COPY target/cookiebot-*.jar cookiebot.jar
ENTRYPOINT [ "java", "-jar", "cookiebot.jar" ]
