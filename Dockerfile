FROM amazoncorretto:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "test@ristopizza315.it", "pippo@russi.ovh", "pluto@russi.ovh"]