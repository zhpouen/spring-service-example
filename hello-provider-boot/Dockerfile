FROM openjdk:17-jdk

WORKDIR /app

COPY target/hello-provider-boot-0.0.1-SNAPSHOT.jar /app

EXPOSE 30000

CMD ["java", "-jar", "hello-provider-boot-0.0.1-SNAPSHOT.jar"]
