# README #


### What is this repository for? ###
**Summary**
This is the RESTful base web server.
it's configured as a MAVEN project written in JAVA using the following technologies:

1. Spring as restcontroller provider
2. Hibernate as JPA provider
3. Spring data over Hibernate to manage repositories
4. JsonDoc to autogenerate the API documentation
5. MAVEN to manage dependencies and package the project
...

### How do I get set up? ###

To start working on this project you will need several technologies:

1. Download and install JAVA JDK 1.8 from Oracle[ web site](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
(Don't forget to set up your java home, you will need it)
2. Download and install MAVEN from Apache download  [web site](https://maven.apache.org/download.cgi)
3. Check that everything is running correctly checking your versions in a command prompt (java -version and mvn -v for java and maven respectively)
4. Download and install a Java application server of your preference, we suggest you to use Tomcat 8 ([web site](http://tomcat.apache.org/download-80.cgi)) because is the one being used in the QA and Production environments. 
5. Download java IDE, we suggest[ Eclipse](https://www.eclipse.org/downloads/), but feel free to use any in which you feel comfortable.
6. Clone the repository using the control versión software of your preference.

If you used all the tools that we recommended at this point you just have to import in eclipse a "Maven Project" (browse to the directory where you cloned the this repository)
(Maven will download all the dependencies for you) and run the project in the tomcat server using the Eclipse Server tool.




* Configuration


### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

** Repo owner or admin**
Christian Leon, Eduardo Lorenzo

** Other community or team contact**
