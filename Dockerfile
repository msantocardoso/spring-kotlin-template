FROM gradle:6.1.1 as builder

USER root

ENV APP_DIR /app
WORKDIR $APP_DIR

COPY build.gradle.kts $APP_DIR/
COPY settings.gradle.kts $APP_DIR/

COPY . $APP_DIR

RUN gradle build -x test

USER guest

# -----------------------------------------------------------------------------	

FROM openjdk:13-slim-buster

WORKDIR /app

COPY --from=builder /app/init.sh /app
COPY --from=builder /app/build/libs/spring-kotlin-template-*.jar /app/

EXPOSE 8080

ENTRYPOINT ["sh", "init.sh"]
