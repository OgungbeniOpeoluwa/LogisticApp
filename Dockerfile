FROM maven:3.8.7 as build
COPY . .
RUN mvn -B clean package -DskipTests

FROM openjdk:21
COPY --from=build ./target/*.jar logistic.jar
ENTRYPOINT ["java", "-jar","logistic.jar"]