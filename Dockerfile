FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
EXPOSE 8080
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.data.mongodb.uri=mongodb://mongodb:27017/oauth2  -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
