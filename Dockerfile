FROM java11
EXPOSE 8082
ADD sendelapa-0.0.1-SNAPSHOT.jar sendelapa.jar
ENTRYPOINT [java, -jar, sendelapa.jar]