FROM java:8-alpine

LABEL maintainer="zeddwang@foxmail.com"

ENV WORK_PATH /app
ENV APP_NAME jeebbs-0.0.1-SNAPSHOT.jar
ENV APP_VERSION 0.0.1-SNAPSHOT

EXPOSE 8080

VOLUME ["/app", "/var/log/jeebbs"]

COPY $APP_NAME $WORK_PATH/

WORKDIR $WORK_PATH

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom"]

CMD ["-jar", "jeebbs-0.0.1-SNAPSHOT.jar"]