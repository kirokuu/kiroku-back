FROM openjdk:21
WORKDIR /app
COPY build/libs/kiroku-0.0.1-SNAPSHOT.jar kirocu.jar
CMD ["java", "-jar", "kirocu.jar"]
LABEL authors="jeonbyeong-il"
