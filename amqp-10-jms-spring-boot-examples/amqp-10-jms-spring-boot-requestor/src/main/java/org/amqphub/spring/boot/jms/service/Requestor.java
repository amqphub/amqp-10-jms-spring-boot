/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.amqphub.spring.boot.jms.service;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * Simple Hello World example that sends and receives a message
 */
@SpringBootApplication
@EnableJms
public class Requestor {

    public static void main(String[] args) {
        SpringApplication.run(Requestor.class, args);
    }

    @Component
    static class MessageProducer implements CommandLineRunner {

        private static final Logger LOG = LoggerFactory.getLogger(MessageProducer.class);

        private final UUID correlationID = UUID.randomUUID();

        @Autowired
        private JmsTemplate jmsTemplate;

        @Override
        public void run(String... strings) throws Exception {
            final String messageText = "Hello World";
            LOG.info("============= Sending " + messageText);
            this.jmsTemplate.send("example", new MessageCreator() {

                @Override
                public Message createMessage(Session session) throws JMSException {
                    Queue replyQueue = session.createQueue("reply-queue");

                    TextMessage message = session.createTextMessage(messageText);

                    message.setJMSCorrelationID(correlationID.toString());
                    message.setJMSReplyTo(replyQueue);

                    return message;
                }
            });
        }
    }

    @Component
    static class MessageHandler {

        private static final Logger LOG = LoggerFactory.getLogger(MessageHandler.class);

        @JmsListener(destination = "reply-queue")
        public void processMsg(String message) {
            LOG.info("============= Received: " + message);
        }
    }
}
