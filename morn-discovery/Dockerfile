FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /morn-discovery

WORKDIR /morn-discovery

EXPOSE 8848

ADD ./target/morn-discovery.jar ./

CMD sleep 30;java -Djava.security.egd=file:/dev/./urandom -jar morn-discovery.jar


