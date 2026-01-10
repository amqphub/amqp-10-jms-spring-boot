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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import jakarta.jms.ConnectionFactory;

public class AMQP10JMSPooledAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @AfterEach
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void testPooledConenctionOptionOverridesCachingConnectionFactory() {
        load(EmptyConfiguration.class,
            "spring.jms.cache.enabled=true",
            "amqphub.amqp10jms.pool.enabled=true");

        JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);
        ConnectionFactory connectionFactory =
            this.context.getBean(ConnectionFactory.class);

        assertTrue(connectionFactory instanceof JmsPoolConnectionFactory);
        assertEquals(jmsTemplate.getConnectionFactory(), connectionFactory);
    }

    @Test
    public void testDefaultsToPoolDefaults() {
        load(EmptyConfiguration.class,
            "amqphub.amqp10jms.pool.enabled=true");

        JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);
        ConnectionFactory connectionFactory =
            this.context.getBean(ConnectionFactory.class);

        assertTrue(connectionFactory instanceof JmsPoolConnectionFactory);
        assertEquals(jmsTemplate.getConnectionFactory(), connectionFactory);

        JmsPoolConnectionFactory pooledFactory = (JmsPoolConnectionFactory) connectionFactory;

        assertTrue(pooledFactory.isBlockIfSessionPoolIsFull());
        assertTrue(pooledFactory.isUseAnonymousProducers());
        assertFalse(pooledFactory.isUseProviderJMSContext());

        assertEquals(1, pooledFactory.getMaxConnections());
        assertEquals(500, pooledFactory.getMaxSessionsPerConnection());
        assertEquals(0, pooledFactory.getExplicitProducerCacheSize());

        assertEquals(30000, pooledFactory.getConnectionIdleTimeout());
        assertEquals(-1, pooledFactory.getConnectionCheckInterval());
        assertEquals(-1, pooledFactory.getBlockIfSessionPoolIsFullTimeout());

        assertTrue(pooledFactory.getConnectionFactory() instanceof JmsConnectionFactory);

        JmsConnectionFactory qpidJmsFactory = (JmsConnectionFactory) pooledFactory.getConnectionFactory();

        assertEquals("amqp://localhost:5672", qpidJmsFactory.getRemoteURI());
        assertNull(qpidJmsFactory.getUsername());
        assertNull(qpidJmsFactory.getPassword());
    }

    @Test
    public void testConfiguredJmsPoolConnectionFactory() {
        load(EmptyConfiguration.class,
            "amqphub.amqp10jms.pool.blockIfSessionPoolIsFull=false",
            "amqphub.amqp10jms.pool.useAnonymousProducers=false",
            "amqphub.amqp10jms.pool.useProviderJMSContext=true",
            "amqphub.amqp10jms.pool.maxConnections=2",
            "amqphub.amqp10jms.pool.maxSessionsPerConnection=100",
            "amqphub.amqp10jms.pool.explicitProducerCacheSize=5",
            "amqphub.amqp10jms.pool.connectionIdleTimeout=100",
            "amqphub.amqp10jms.pool.connectionCheckInterval=50",
            "amqphub.amqp10jms.pool.blockIfSessionPoolIsFullTimeout=3000",
            "amqphub.amqp10jms.pool.enabled=true");

        ConnectionFactory connectionFactory =
            this.context.getBean(ConnectionFactory.class);

        assertTrue(connectionFactory instanceof JmsPoolConnectionFactory);

        JmsPoolConnectionFactory pooledFactory = (JmsPoolConnectionFactory) connectionFactory;

        assertFalse(pooledFactory.isBlockIfSessionPoolIsFull());
        assertFalse(pooledFactory.isUseAnonymousProducers());
        assertTrue(pooledFactory.isUseProviderJMSContext());

        assertEquals(2, pooledFactory.getMaxConnections());
        assertEquals(100, pooledFactory.getMaxSessionsPerConnection());
        assertEquals(5, pooledFactory.getExplicitProducerCacheSize());

        assertEquals(100, pooledFactory.getConnectionIdleTimeout());
        assertEquals(50, pooledFactory.getConnectionCheckInterval());
        assertEquals(3000, pooledFactory.getBlockIfSessionPoolIsFullTimeout());

        assertTrue(pooledFactory.getConnectionFactory() instanceof JmsConnectionFactory);
    }

    @Test
    public void testConfiguredJmsPoolConnectionFactoryUsingSpringNames() {
        load(EmptyConfiguration.class,
            "amqphub.amqp10jms.pool.blockIfFull=false",
            "amqphub.amqp10jms.pool.blockIfFullTimeout=3000",
            "amqphub.amqp10jms.pool.idleTimeout=100",
            "amqphub.amqp10jms.pool.timeBetweenExpirationCheck=50",
            "amqphub.amqp10jms.pool.maxConnections=2",
            "amqphub.amqp10jms.pool.maxSessionsPerConnection=100",
            "amqphub.amqp10jms.pool.useAnonymousProducers=false",
            // Qpid JMS Starter specific names
            "amqphub.amqp10jms.pool.useProviderJMSContext=true",
            "amqphub.amqp10jms.pool.explicitProducerCacheSize=5",
            "amqphub.amqp10jms.pool.enabled=true");

        ConnectionFactory connectionFactory =
            this.context.getBean(ConnectionFactory.class);

        assertTrue(connectionFactory instanceof JmsPoolConnectionFactory);

        JmsPoolConnectionFactory pooledFactory = (JmsPoolConnectionFactory) connectionFactory;

        assertFalse(pooledFactory.isBlockIfSessionPoolIsFull());
        assertFalse(pooledFactory.isUseAnonymousProducers());
        assertTrue(pooledFactory.isUseProviderJMSContext());

        assertEquals(2, pooledFactory.getMaxConnections());
        assertEquals(100, pooledFactory.getMaxSessionsPerConnection());
        assertEquals(5, pooledFactory.getExplicitProducerCacheSize());

        assertEquals(100, pooledFactory.getConnectionIdleTimeout());
        assertEquals(50, pooledFactory.getConnectionCheckInterval());
        assertEquals(3000, pooledFactory.getBlockIfSessionPoolIsFullTimeout());

        assertTrue(pooledFactory.getConnectionFactory() instanceof JmsConnectionFactory);
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
