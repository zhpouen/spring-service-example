FROM openjdk:17-jdk

WORKDIR /app

COPY target/hello-flowserver-boot-0.0.1-SNAPSHOT.jar /app

EXPOSE 30003

CMD ["java", "-jar", "hello-flowserver-boot-0.0.1-SNAPSHOT.jar"]
