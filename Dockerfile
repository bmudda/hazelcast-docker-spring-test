FROM java:8u92-jre-alpine
MAINTAINER bonaya.mudda@gmail.com

######################
# IMAGE DETAILS
######################

VOLUME /tmp

RUN mkdir -p /hzdocker

# Install zip/curl/bash
RUN apk add --update zip && rm -rf /var/cache/apk/*
RUN apk add --update curl && rm -rf /var/cache/apk/*
RUN apk add --update bash && rm -rf /var/cache/apk/*

COPY build/libs/hazelcast-docker-test-1.0-RC1.jar /hzdocker/hazelcast-docker-test.jar

EXPOSE 5701 5702
 
COPY docker-entrypoint.sh /entrypoint.sh 
ENTRYPOINT ["/entrypoint.sh"]

# NOTE: java -jar /dockertest/hazelcast-docker-test.jar 
CMD ["java"]
 
