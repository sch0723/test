FROM openjdk:11-jdk-oraclelinux7
WORKDIR /app
ADD demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar"]
CMD ["app.jar"]
