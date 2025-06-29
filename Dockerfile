FROM openjdk:21
WORKDIR /app
COPY build/libs/*.jar kirocu.jar
CMD ["java", "-jar", "kirocu.jar"]

