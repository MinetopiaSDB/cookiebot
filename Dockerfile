FROM eclipse-temurin:25-jdk-alpine

WORKDIR /srv/cookiebot

COPY target/cookiebot-*.jar cookiebot.jar
ENTRYPOINT [ "java", "-jar", "cookiebot.jar" ]
