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

First build and install all the modules as detailed above (if running against a
source checkout/release, rather than against released binaries) and then consult the
README in the amqp-10-jms-spring-boot-examples module itself.

## Spring configuration

The following options can be used in an 'application-properties' file to configure you
Spring Boot project.

#### JMS Connection Configuration

+ **amqphub.amqp10jms.remote-url**  The URL that the AMQP JMS client should use when it attempts to establish a new connection to a remote AMQP peer.

The basic format of the clients Connection URI is as follows:

      amqp[s]://hostname:port[?option=value[&option2=value...]]

or for WebSocket connections:

     amqpws[s]://hostname:port[/path][?option=value[&option2=value...]]

Where the *amqps* and *amqpwss* scheme is specified to use SSL/TLS, the hostname segment from the URI can be used by the JVM for the
TLS SNI (Server Name Indication) extension in order to communicate the desired server hostname during a TLS handshake.
The SNI extension will be automatically included if a Fully Qualified name (e.g myhost.mydomain) is specified, but not
when an unqualified name (e.g myhost) or bare IP address are used.

The client can be configured with a number of different settings using the URI while defining the ConnectionFactory.

+ **amqphub.amqp10jms.username** User name value used to authenticate the connection
+ **amqphub.amqp10jms.password** The password value used to authenticate the connection
+ **amqphub.amqp10jms.clientId** The ClientID value that is applied to the connection.
+ **amqphub.amqp10jms.receiveLocalOnly** If enabled receive calls with a timeout will only check a consumers local message buffer, otherwise the remote peer is checked to ensure there are really no messages available if the local timeout expires before a message arrives. Default is false, the remote is checked.
+ **amqphub.amqp10jms.receiveNoWaitLocalOnly**  If enabled receiveNoWait calls will only check a consumers local message buffer, otherwise the remote peer is checked to ensure there are really no messages available. Default is false, the remote is checked.

