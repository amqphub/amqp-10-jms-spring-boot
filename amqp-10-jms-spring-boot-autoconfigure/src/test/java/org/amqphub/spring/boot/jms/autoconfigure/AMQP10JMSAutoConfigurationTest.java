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
package org.amqphub.spring.boot.jms.autoconfigure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import jakarta.jms.ConnectionFactory;

/**
 * Test that AMQP 1.0 JMS Auto Configuration works.
 */
public class AMQP10JMSAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @AfterEach
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void testDefaultsToLocalURI() {
        load(EmptyConfiguration.class);

        JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);
        ConnectionFactory connectionFactory =
            this.context.getBean(ConnectionFactory.class);

        assertTrue(connectionFactory instanceof JmsConnectionFactory);

        JmsConnectionFactory qpidJmsFactory = (JmsConnectionFactory) connectionFactory;

        assertEquals(jmsTemplate.getConnectionFactory(), connectionFactory);
        assertEquals("amqp://localhost:5672", qpidJmsFactory.getRemoteURI());
        assertNull(qpidJmsFactory.getUsername());
        assertNull(qpidJmsFactory.getPassword());
    }

    @Test
    public void testCustomConnectionFactorySettings() {
        load(EmptyConfiguration.class,
             "amqphub.amqp10jms.remote-url=amqp://127.0.0.1:5672",
             "amqphub.amqp10jms.username=foo",
             "amqphub.amqp10jms.password=bar");

        JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);
        JmsConnectionFactory connectionFactory =
            this.context.getBean(JmsConnectionFactory.class);

        assertEquals(jmsTemplate.getConnectionFactory(), connectionFactory);
        assertEquals("amqp://127.0.0.1:5672", connectionFactory.getRemoteURI());
        assertEquals("foo", connectionFactory.getUsername());
        assertEquals("bar", connectionFactory.getPassword());
    }

    @Test
    public void testReceiveLocalOnlyOptionsAppliedFromEnv() {
        load(EmptyConfiguration.class,
             "amqphub.amqp10jms.receiveLocalOnly=true",
             "amqphub.amqp10jms.receiveNoWaitLocalOnly=true");

        JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);
        JmsConnectionFactory connectionFactory =
            this.context.getBean(JmsConnectionFactory.class);

        assertEquals(jmsTemplate.getConnectionFactory(), connectionFactory);

        assertTrue(connectionFactory.isReceiveLocalOnly());
        assertTrue(connectionFactory.isReceiveNoWaitLocalOnly());
    }

    @Test
    public void testReceiveLocalOnlyOptionsAppliedFromEnvOverridesURI() {
        load(EmptyConfiguration.class,
             "amqphub.amqp10jms.remote-url=amqp://127.0.0.1:5672" +
                 "?jms.receiveLocalOnly=false&jms.receiveNoWaitLocalOnly=false",
             "amqphub.amqp10jms.receiveLocalOnly=true",
             "amqphub.amqp10jms.receiveNoWaitLocalOnly=true");

        JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);
        JmsConnectionFactory connectionFactory =
            this.context.getBean(JmsConnectionFactory.class);

        assertEquals(jmsTemplate.getConnectionFactory(), connectionFactory);

        assertTrue(connectionFactory.isReceiveLocalOnly());
        assertTrue(connectionFactory.isReceiveNoWaitLocalOnly());
    }

    @Configuration
    static class EmptyConfiguration {}

    private void load(Class<?> config, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(config);
        applicationContext.register(AMQP10JMSAutoConfiguration.class, JmsAutoConfiguration.class);

        TestPropertyValues.of(environment)
                .applyTo(applicationContext);

        applicationContext.refresh();
        this.context = applicationContext;
    }
}
