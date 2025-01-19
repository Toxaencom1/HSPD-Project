FROM openjdk:17

WORKDIR /app

COPY target/HSPD-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh