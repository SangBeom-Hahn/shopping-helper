FROM openjdk:17-jdk-slim-buster

WORKDIR /app

ENV SPRING_PROFILE=${SPRING_PROFILE}
COPY ./backend/build/libs/backend-0.0.1-SNAPSHOT.jar /app/app.jar
COPY ./src/main/resources/credentials.json /app/server/resources/credentials.json

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "/app.jar"]