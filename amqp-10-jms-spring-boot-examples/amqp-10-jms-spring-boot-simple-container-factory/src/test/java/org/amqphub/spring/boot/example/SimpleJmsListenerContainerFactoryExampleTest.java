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
package org.amqphub.spring.boot.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.core.server.Queue;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Simple Hello World example that sends and receives a message using both the
 * Hello World Command Line instance and a manual call to show either can work.
 */
@SpringBootTest
public class SimpleJmsListenerContainerFactoryExampleTest {

    private static EmbeddedActiveMQ server;

    @Autowired
    public SimpleJmsListenerContainerFactoryMessageConsumer consumer;

    @Autowired
    public SimpleJmsListenerContainerFactoryMessageProducer producer;

    @BeforeAll
    public static void setUp() throws Exception {
        server = new EmbeddedActiveMQ();
        server.start();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testMessageIsSent(TestInfo info) throws Exception {
        producer.sendMessage("Hello: " + info.getDisplayName());

        Thread.sleep(20);

        // Should have our send plus the one sent by the run of MessageProducer by Spring
        Queue queueView = getProxyToQueue("example");
        assertEquals(2, queueView.getMessagesAcknowledged());
    }

    public Queue getProxyToQueue(String queueName) {
        return server.getActiveMQServer().locateQueue(SimpleString.toSimpleString(queueName));
     }
}
