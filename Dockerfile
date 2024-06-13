FROM gradle:8-jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*-all.jar /app/kotatsu-syncserver.jar
ENTRYPOINT ["java","-jar","/app/kotatsu-syncserver.jar"]