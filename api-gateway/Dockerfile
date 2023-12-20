FROM openjdk:17
LABEL maintainer="mkhattab@c3s.co"
WORKDIR /app

COPY target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]