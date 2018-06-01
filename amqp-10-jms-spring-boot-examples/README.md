# AMQP 1.0 JMS Spring Boot Examples

This module contains a set of examples that show how to use the
AMQP 1.0 JMS Spring Boot auto-configure and starter to create a new
client application using an AMQP v1.0 JMS client.

## Biulding the examples

The examples are built using maven and require a maven version 3
version to build correctly.

    mvn clean install

## Running the examples

The example application all require that you have an AMQP v1.0
broker running on the local machine and listening on port 5672.

Once you have a broker running then you can run each example as
follows:

### Hello World Example

This example sends a text message to a queue and listens on that
same queue for the sent message.

    cd amqp-10-jms-spring-boot-hello-world
    java -jar target/amqp-10-jms-spring-boot-hello-world-<version>.jar

### Request example

This example sends a request to a queue with a reply-to value set
on the message and waits for a response.

    cd amqp-10-jms-spring-boot-requestor
    java -jar target/amqp-10-jms-spring-boot-requestor-<version>.jar

### Reply Service example

This example listens on a queue for a request message and sends a
response back on the reply-to address set on the incoming request
message.

    cd amqp-10-jms-spring-boot-reply-service
    java -jar target/amqp-10-jms-spring-boot-reply-service-<version>.jar

### Simple Container Factory Example

This example shows one method of overriding the Spring Message Listener
Container type that is used by the JMS Listener bean created in the example.
The example sends a text message to a queue and listens on that same queue
for the sent message using the Spring SimpleJmsMessageListenerContainer that
has been configured to not use transactions.

    cd amqp-10-jms-spring-boot-hello-world
    java -jar target/amqp-10-jms-spring-boot-simple-container-factory-<version>.jar

### Pooled Connection Example

This example demonstrates how to configure the starter to use a pooled
JMS ConnectionFactory to use the same JMS connection to both send a text
message to a queue and listen on that same queue for the sent message.
Normally this would occur on two different JMS Connection instances but
because the pooling option is enabled only one is used.

    cd amqp-10-jms-spring-boot-hello-world
    java -jar target/amqp-10-jms-spring-boot-pooled-connection-<version>.jar

### Custom connection factory configuration example

This example shows how to programmatically configure the Qpid JMS
ConnectionFactory created by this Spring Boot starter using a user
supplied intercepter.  The example then sends a text message to a queue
and listens on that same queue for the sent message.

    cd amqp-10-jms-spring-boot-hello-world
    java -jar target/amqp-10-jms-spring-boot-custom-configuration-<version>.jar
