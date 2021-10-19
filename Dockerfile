FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
MAINTAINER itsampathsl@gmail.com
COPY target/rss-parser-0.0.1-SNAPSHOT.jar rss-parser-0.0.1.jar
ENTRYPOINT ["java","-jar","/rss-parser-0.0.1.jar"]