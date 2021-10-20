# Spring RSSFeed Parser Application

## Running RSSFeed Parser Locally Command Line
RSSFeed Parser is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:


```
git clone https://github.com/sampathsl/rss-parser.git
cd rss-parser
./mvnw package
java -jar target/rss-parser-0.0.1-SNAPSHOT.jar
```

## Running RSSFeed Parser Locally Using Docker
RSSFeed Parser is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can run the application as docker container:

```
git clone https://github.com/sampathsl/rss-parser.git
cd rss-parser
docker-compose up
```

> NOTE: To run docker container as RssParser application you need to install docker in your machine. Sometimes Windows users may not able to run dockerized RssParser application.


After successfully running the application you can access RSSFeed Parser API here: http://localhost:8080/items

<img width="1042" alt="rssfeed-api-screenshot" src="https://github.com/sampathsl/rss-parser/blob/master/docs/api.png">

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```

## Database configuration

In its default configuration, RSSFeed parser uses an in-memory database (H2) which
gets populated at startup with data. The h2 console is automatically exposed at `http://localhost:8080/h2-console`
and it is possible to inspect the content of the database using the `jdbc:h2:mem:rss_data` url.

## Working with RSSFeed Parser in your IDE

### Prerequisites
The following items should be installed in your system:
* Java 11 or newer (full JDK not a JRE).
* git command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE 
  * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, just follow the install process here: https://www.eclipse.org/m2e/
  * [Spring Tools Suite](https://spring.io/tools) (STS)
  * IntelliJ IDEA
  * [VS Code](https://code.visualstudio.com)

### Steps:

1) On the command line
    ```
    git clone https://github.com/sampathsl/rss-parser.git
    ```
2) Inside Eclipse or STS
    ```
    File -> Import -> Maven -> Existing Maven project
    ```

    Then either build on the command line `./mvnw generate-resources` or using the Eclipse launcher (right click on project and `Run As -> Maven install`) to generate the css. Run the application main method by right clicking on it and choosing `Run As -> Java Application`.

3) Inside IntelliJ IDEA
    In the main menu, choose `File -> Open` and select the Petclinic [pom.xml](pom.xml). Click on the `Open` button.

    A run configuration named `RssParserApplication` should have been created for you if you're using a recent Ultimate version. Otherwise, run the application by right clicking on the `RssParserApplication` main class and choosing `Run 'RssParserApplication'`.

4) Navigate to RssParser Application API

    Visit [http://localhost:8080/items](http://localhost:8080/items) in your browser.


## Looking for something in particular?

|Spring Boot Configuration | Class or Java property files  |
|--------------------------|---|
|The Main Class | [RssParserApplication](https://github.com/sampathsl/rss-parser/blob/master/src/main/java/com/gifted/rss/RssParserApplication.java) |
|Properties Files | [application.properties](https://github.com/sampathsl/rss-parser/blob/master/src/main/resources/application.properties) |

# License

The Spring RSSFeed Parser sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).