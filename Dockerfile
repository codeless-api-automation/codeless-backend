FROM amazoncorretto:11-alpine-jdk
MAINTAINER apisentinel

COPY target/codeless-backend-1.0-SNAPSHOT.jar codeless-backend-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/codeless-backend-1.0-SNAPSHOT.jar"]