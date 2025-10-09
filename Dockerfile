FROM gradle:9-jdk21 AS build
WORKDIR /home/gradle/app
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle shadowJar --no-daemon



FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /home/gradle/app/build/libs/*-all.jar /app/kotatsu-syncserver.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/kotatsu-syncserver.jar"]
