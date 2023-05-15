# Running the AMQP 1.0 JMS Spring Boot Hello World with TLS

This example demonstrates a connection to the an AMQP broker. The example creates a sender and a receiver on a Queue and sends a simple message and then awaits the remote broker to send that message to the consumer.

## Building the example

The examples is built using Apache maven and requires maven version 3 to build correctly.

    mvn clean install

## Running the example

The example application requires that you have an AMQP v1.0 broker running on the local machine and listening for non-SSL examples your broker should normally be listening on port '5672'. If the running broker is listening on non-default AMQP ports then the application properties file should be updated with the correct connection configuration.

    cd amqp-10-jms-spring-boot-hello-world
    java -jar target/amqp-10-jms-spring-boot-hello-world-<version>.jar

## Running the example with TLS enabled

In order for this to work you have to generate a keypair, to make this easier the example ships with a script that will create a certificate for use with your broker instance.

    cd amqp-10-jms-spring-boot-hello-world/src/main/resources/
    source README.txt

For the rest of these instruction we assume that the AMQP v1.0 broker in use is ActiveMQ Artemis for other brokers you will need to refer to the broker documentation on how SSL is configured. Add the following acceptor configuration to your broker configuration if using the included certificate generation script:

    <!-- AMQPS Acceptor. -->
    <acceptor name="amqps">tcp://0.0.0.0:5673?sslEnabled=true;keyStorePath=server-keystore.p12;keyStorePassword=securepass;protocols=AMQP</acceptor>

**[NOTE]** You need to change the path of the file `server-keystore.p12` if you are placing it somewhere else than next to your `broker.xml`

On the client site you need to change the `application.properties` file:

    amqphub.amqp10jms.remote-url=amqps://localhost:5673?transport.trustStoreLocation=src/main/resources/server-ca-truststore.p12&transport.trustStorePassword=securepass

Once you have a broker running then you can run the example as follows:

    cd amqp-10-jms-spring-boot-hello-world
    java -jar target/amqp-10-jms-spring-boot-hello-world-<version>.jar

This example sends a text message to a queue and listens on that same queue for the sent message.