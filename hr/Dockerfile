FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY ./build/libs/hr-0.0.1-SNAPSHOT.jar /app/hr.jar

EXPOSE 8083

CMD ["java", "-jar", "hr.jar", "-server.port=$PORT"]
