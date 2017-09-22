FROM docker.elastic.co/logstash/logstash:5.2.1
COPY *.conf /usr/src/application/
COPY patterns /usr/src/application/patterns
WORKDIR /usr/src/application
CMD ["logstash", "-f", "*.conf"]