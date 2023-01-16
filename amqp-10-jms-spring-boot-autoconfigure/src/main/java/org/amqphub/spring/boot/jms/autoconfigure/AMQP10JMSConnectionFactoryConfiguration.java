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

import org.apache.commons.pool2.PooledObject;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.JmsPoolConnectionFactoryFactory;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import jakarta.jms.ConnectionFactory;

/**
 * Configuration for Qpid JMS {@link ConnectionFactory} instance used when configuring
 * the client beans.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ConnectionFactory.class)
public class AMQP10JMSConnectionFactoryConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(CachingConnectionFactory.class)
    @ConditionalOnProperty(prefix = "amqphub.amqp10jms.pool", name = "enabled", havingValue = "false", matchIfMissing = true)
    static class SimpleConnectionFactoryConfiguration {

        private final AMQP10JMSProperties properties;

        private final ListableBeanFactory beanFactory;

        SimpleConnectionFactoryConfiguration(AMQP10JMSProperties properties, ListableBeanFactory beanFactory) {
            this.properties = properties;
            this.beanFactory = beanFactory;
        }

        @Bean(name = "jmsConnectionFactory")
        @ConditionalOnProperty(prefix = "spring.jms.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
        CachingConnectionFactory cachingJmsConnectionFactory(JmsProperties jmsProperties) {
            JmsProperties.Cache cacheProperties = jmsProperties.getCache();
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory(createConnectionFactory());
            connectionFactory.setCacheConsumers(cacheProperties.isConsumers());
            connectionFactory.setCacheProducers(cacheProperties.isProducers());
            connectionFactory.setSessionCacheSize(cacheProperties.getSessionCacheSize());
            return connectionFactory;
        }

        @Bean(name = "jmsConnectionFactory")
        @ConditionalOnProperty(prefix = "spring.jms.cache", name = "enabled", havingValue = "false", matchIfMissing = false)
        JmsConnectionFactory jmsConnectionFactory() {
            return createConnectionFactory();
        }

        private JmsConnectionFactory createConnectionFactory() {
            return new AMQP10JMSConnectionFactoryFactory(this.beanFactory, this.properties)
                    .createConnectionFactory(JmsConnectionFactory.class);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ JmsPoolConnectionFactory.class, PooledObject.class })
    @ConditionalOnProperty(prefix = "amqphub.amqp10jms.pool", name = "enabled", havingValue = "true")
    static class PooledConnectionFactoryConfiguration {

        @Bean(destroyMethod = "stop")
        JmsPoolConnectionFactory jmsConnectionFactory(ListableBeanFactory beanFactory, AMQP10JMSProperties properties) {
            JmsConnectionFactory connectionFactory = new AMQP10JMSConnectionFactoryFactory(beanFactory, properties)
                .createConnectionFactory(JmsConnectionFactory.class);
            JmsPoolConnectionFactory poolCf = new JmsPoolConnectionFactoryFactory(properties.getPool())
                .createPooledConnectionFactory(connectionFactory);

            poolCf.setUseProviderJMSContext(properties.getPool().isUseProviderJMSContext());
            poolCf.setExplicitProducerCacheSize(properties.getPool().getExplicitProducerCacheSize());

            return poolCf;
        }
    }
}
