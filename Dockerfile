FROM eclipse-temurin:17-jdk-alpine

WORKDIR /srv/cookiebot

COPY target/cookiebot-*.jar cookiebot.jar
ENTRYPOINT [ "java", "-jar", "cookiebot.jar" ]
