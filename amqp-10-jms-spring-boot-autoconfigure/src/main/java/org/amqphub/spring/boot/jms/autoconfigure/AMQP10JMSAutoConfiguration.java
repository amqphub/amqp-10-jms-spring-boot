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

import java.util.List;

import javax.jms.ConnectionFactory;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto Configuration class for the AMQP 1.0 JMS client allowing for the addition
 * of connection pooling based on configuration.
 */
@Configuration
@AutoConfigureBefore(JmsAutoConfiguration.class)
@AutoConfigureAfter({JndiConnectionFactoryAutoConfiguration.class})
@ConditionalOnMissingBean(ConnectionFactory.class)
@ConditionalOnClass({ConnectionFactory.class, JmsConnectionFactory.class})
@EnableConfigurationProperties(AMQP10JMSProperties.class)
public class AMQP10JMSAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "amqphub.amqp10jms.pool", name = "enabled", havingValue = "false", matchIfMissing = true)
    public JmsConnectionFactory jmsConnectionFactory(AMQP10JMSProperties properties,
            ObjectProvider<List<AMQP10JMSConnectionFactoryCustomizer>> factoryCustomizers) {

        // Create new connection factory factory with optional user customizer
        return new AMQP10JMSConnectionFactoryFactory(properties, factoryCustomizers.getIfAvailable())
            .createConnectionFactory(JmsConnectionFactory.class);
    }

    @Configuration
    @ConditionalOnClass(JmsPoolConnectionFactory.class)
    static class PooledConnectionFactoryConfiguration {

        @Bean(destroyMethod = "stop")
        @ConditionalOnProperty(prefix = "amqphub.amqp10jms.pool", name = "enabled", havingValue = "true", matchIfMissing = false)
        public JmsPoolConnectionFactory pooledJmsConnectionFactory(
                AMQP10JMSProperties properties,
                ObjectProvider<List<AMQP10JMSConnectionFactoryCustomizer>> factoryCustomizers) {

            JmsPoolConnectionFactory pooledConnectionFactory = new JmsPoolConnectionFactory();
            pooledConnectionFactory.setConnectionFactory(
                new AMQP10JMSConnectionFactoryFactory(properties, factoryCustomizers.getIfAvailable())
                    .createConnectionFactory(JmsConnectionFactory.class));

            AMQP10JMSProperties.Pool pool = properties.getPool();

            pool.configurePooledFactory(pooledConnectionFactory);

            return pooledConnectionFactory;
        }
    }
}
