FROM openjdk:17-jdk-slim-buster

ENV SPRING_PROFILE=${SPRING_PROFILE}
COPY ./backend/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "/app.jar"]