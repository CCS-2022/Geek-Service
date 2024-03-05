FROM openjdk:latest

WORKDIR /app

#COPY . .

COPY ./build/libs/*-SNAPSHOT.jar geek-backend.jar

#WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "geek-backend.jar" ]
