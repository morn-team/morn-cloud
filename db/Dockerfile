FROM mysql:8.0.20

MAINTAINER lengleng(wangiegie@gmail.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./morn_upms.sql /docker-entrypoint-initdb.d

COPY ./morn_codegen.sql /docker-entrypoint-initdb.d

COPY ./morn_config.sql /docker-entrypoint-initdb.d
