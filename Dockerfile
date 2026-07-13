FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
RUN ./mvnw -q -DskipTests clean package -pl bootstrap -am

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/bootstrap/target/bootstrap-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
