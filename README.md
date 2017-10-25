# AMQP 1.0 JMS Spring Boot

This project provides an easy way to get your Spring based JMS messaging
application using AMQP v1.0 up and running quickly.

The project provides a Spring Boot based auto-configuration module which
allows your application to quickly grab a JMSTemplate that is properly
configured to use the Qpid JMS AMQP 1.0 client as the underlying transport.
The project also provides a Spring Boot Starter that allows you to create a
Spring based application that be run from the command line using just a
java -jar invocation.

To use the AMQP 1.0 JMS starter in your projects you can include the maven
dependency in your project pom file:

    <dependency>
      <groupId>org.amqphub.spring</groupId>
      <artifactId>amqp-10-jms-spring-boot-starter</artifactId>
      <version>${current-version}</version>
    </dependency>

## Building the code

The project requires Maven 3. Some example commands follow.

Clean previous builds output and install all modules to local repository without
running the tests:

    mvn clean install -DskipTests

Install all modules to the local repository after running all the tests:

    mvn clean install

Perform a subset tests on the packaged release artifacts without
installing:

    mvn clean verify -Dtest=TestNamePattern*

## Examples

First build and install all the modules as detailed above (if running against a source checkout/release, rather than against released binaries) and then consult the README in the amqp-10-jms-spring-boot-examples module itself.