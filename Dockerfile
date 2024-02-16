FROM openjdk:21
EXPOSE 8080
ADD target/smart-serve-0.0.1-SNAPSHOT.jar smart-serve-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/smart-serve-0.0.1-SNAPSHOT.jar"]
