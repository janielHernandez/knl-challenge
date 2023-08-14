FROM openjdk:17-jdk-alpine as builder

ARG MSVC_NAME=external.call
WORKDIR /app/$MSVC_NAME

COPY ./../pom.xml /app
COPY ./../$MSVC_NAME/.mvn ./.mvn
COPY ../$MSVC_NAME/mvnw .
COPY ../$MSVC_NAME/pom.xml .

COPY ../elevator.common/src /home/commons/src
COPY ../elevator.common/pom.xml /home/commons
RUN ./mvnw -f /home/commons/pom.xml clean install -Dmaven.test.skip=true

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline
COPY ../external.call/src ./src
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/external.call/target/external.call-0.0.1-SNAPSHOT.jar .

ENV PORT 5000
EXPOSE $PORT

#ENTRYPOINT  stricter
CMD ["java", "-jar", "external.call-0.0.1-SNAPSHOT.jar"]