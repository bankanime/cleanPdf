FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 5005
COPY target/cleanPdf-0.0.1-alpha.jar cleanPdf-0.0.1-alpha.jar
CMD ["java", "-jar", "cleanPdf-0.0.1-alpha.jar"]