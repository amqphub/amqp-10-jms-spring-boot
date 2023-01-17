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
import org.springframework.beans.factory.ObjectProvider;
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
 * the client beans. If the connection pooling option is enabled in the {@link AMQP10JMSProperties}
 * then that is always preferred over the Spring {@link CachingConnectionFactory} which is
 * used whenever pooling is not enabled unless explicitly disabled.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ConnectionFactory.class)
public class AMQP10JMSConnectionFactoryConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(CachingConnectionFactory.class)
    @ConditionalOnProperty(prefix = "amqphub.amqp10jms.pool", name = "enabled", havingValue = "false", matchIfMissing = true)
    static class SimpleConnectionFactoryConfiguration {

        @Bean(name = "jmsConnectionFactory")
        @ConditionalOnProperty(prefix = "spring.jms.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
        CachingConnectionFactory cachingJmsConnectionFactory(
            AMQP10JMSProperties properties, ObjectProvider<AMQP10JMSConnectionFactoryCustomizer> factoryCustomizers, JmsProperties jmsProperties) {

            JmsProperties.Cache cacheProperties = jmsProperties.getCache();
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
                createQpidJMSConnectionFactory(properties, factoryCustomizers));
            connectionFactory.setCacheConsumers(cacheProperties.isConsumers());
            connectionFactory.setCacheProducers(cacheProperties.isProducers());
            connectionFactory.setSessionCacheSize(cacheProperties.getSessionCacheSize());
            return connectionFactory;
        }

        @Bean(name = "jmsConnectionFactory")
        @ConditionalOnProperty(prefix = "spring.jms.cache", name = "enabled", havingValue = "false", matchIfMissing = false)
        JmsConnectionFactory jmsConnectionFactory(
            AMQP10JMSProperties properties, ObjectProvider<AMQP10JMSConnectionFactoryCustomizer> factoryCustomizers, JmsProperties jmsProperties) {

            return createQpidJMSConnectionFactory(properties, factoryCustomizers);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ JmsPoolConnectionFactory.class, PooledObject.class })
    @ConditionalOnProperty(prefix = "amqphub.amqp10jms.pool", name = "enabled", havingValue = "true")
    static class PooledConnectionFactoryConfiguration {

        @Bean(destroyMethod = "stop")
        JmsPoolConnectionFactory jmsConnectionFactory(
            AMQP10JMSProperties properties, ObjectProvider<AMQP10JMSConnectionFactoryCustomizer> factoryCustomizers, JmsProperties jmsProperties) {

            ConnectionFactory connectionFactory = createQpidJMSConnectionFactory(properties, factoryCustomizers);
            JmsPoolConnectionFactory poolCf = new JmsPoolConnectionFactoryFactory(properties.getPool())
                .createPooledConnectionFactory(connectionFactory);

            poolCf.setUseProviderJMSContext(properties.getPool().isUseProviderJMSContext());
            poolCf.setExplicitProducerCacheSize(properties.getPool().getExplicitProducerCacheSize());

            return poolCf;
        }
    }

    private static JmsConnectionFactory createQpidJMSConnectionFactory(AMQP10JMSProperties properties, ObjectProvider<AMQP10JMSConnectionFactoryCustomizer> factoryCustomizers) {
        return new AMQP10JMSConnectionFactoryFactory(properties, factoryCustomizers)
            .createConnectionFactory(JmsConnectionFactory.class);
    }
}
