FROM openjdk:21
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ADD target/smart-serve-0.0.1-SNAPSHOT.jar smart-serve-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/smart-serve-0.0.1-SNAPSHOT.jar"]
