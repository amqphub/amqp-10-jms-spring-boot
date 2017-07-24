# amqp-10-jms-requestor

Simple Request Application used for test the qpid-jms-reply-service

## Building and running the project

Build the project from the top level directory using 'mvn clean install'

Setup you environment variables to point to the host and port of the AMQP broker or if you
don't set them the defaults for host and port are "localhost" and 5672 respectively.

    MESSAGING_SERVICE_HOST = "localhost"
    MESSAGING_SERVICE_PORT = 5672

Run the project using the command line:

    linux:   java -jar amqp-10-jms-requestor/target/amqp-10-jms-requestor-0.1.0-SNAPSHOT.jar

    windows: java -jar amqp-10-jms-requestor\target\amqp-10-jms-requestor-0.1.0-SNAPSHOT.jar

For now you need to stop it via a **Ctrl+C**