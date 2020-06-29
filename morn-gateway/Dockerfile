FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /morn-gateway

WORKDIR /morn-gateway

EXPOSE 9999

ADD ./target/morn-gateway.jar ./

CMD sleep 60;java -Djava.security.egd=file:/dev/./urandom -jar morn-gateway.jar
