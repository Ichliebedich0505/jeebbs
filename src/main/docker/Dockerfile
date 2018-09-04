FROM java:8-alpine

LABEL maintainer="zeddwang@foxmail.com"

ENV WORK_PATH /app
ENV APP_NAME @project.build.finalName@.@project.packaging@
ENV APP_VERSION @project.version@

EXPOSE 8080

VOLUME ["/app", "/var/log"]

COPY $APP_NAME $WORK_PATH/

WORKDIR $WORK_PATH

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom"]

CMD ["-jar", "@project.build.finalName@.@project.packaging@"]