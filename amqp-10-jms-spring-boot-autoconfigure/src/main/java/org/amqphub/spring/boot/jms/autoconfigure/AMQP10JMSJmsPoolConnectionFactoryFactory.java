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

import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.boot.autoconfigure.jms.JmsPoolConnectionFactoryFactory;

import jakarta.jms.ConnectionFactory;

/**
 * Factory wrapper class that servers as a proxy for the creation of a {@link JmsPoolConnectionFactory}
 * such that the code does not need a direct dependency on the class when creating an AMQP connection
 * factory instance.
 */
public class AMQP10JMSJmsPoolConnectionFactoryFactory extends JmsPoolConnectionFactoryFactory {

    private final AMQP10JMSJmsPoolConnectionFactoryProperties properties;

    public AMQP10JMSJmsPoolConnectionFactoryFactory(AMQP10JMSJmsPoolConnectionFactoryProperties properties) {
        super(properties);

        this.properties = properties;
    }

    /**
     * Create a {@link JmsPoolConnectionFactory} that wraps the provided {@link ConnectionFactory} and
     * applies configuration from the pooled connection factory properties instance assigned to this
     * instance.
     *
     * @param connectionFactory the connection factory to wrap in a pooling instance.
     *
     * @return a new fully configured pooled JMS connection factory
     */
    @Override
    public JmsPoolConnectionFactory createPooledConnectionFactory(ConnectionFactory connectionFactory) {
        final JmsPoolConnectionFactory cf = new JmsPoolConnectionFactory();

        cf.setConnectionFactory(connectionFactory);
        cf.setUseProviderJMSContext(properties.isUseProviderJMSContext());
        cf.setExplicitProducerCacheSize(properties.getExplicitProducerCacheSize());
        cf.setUseAnonymousProducers(properties.isUseAnonymousProducers());
        cf.setMaxConnections(properties.getMaxConnections());
        cf.setMaxSessionsPerConnection(properties.getMaxSessionsPerConnection());
        cf.setBlockIfSessionPoolIsFull(properties.isBlockIfFull());

        if (properties.getBlockIfFullTimeout() != null) {
            cf.setBlockIfSessionPoolIsFullTimeout(properties.getBlockIfFullTimeout().toMillis());
        }
        if (properties.getIdleTimeout() != null) {
            cf.setConnectionIdleTimeout((int) properties.getIdleTimeout().toMillis());
        }
        if (properties.getTimeBetweenExpirationCheck() != null) {
            cf.setConnectionCheckInterval(properties.getTimeBetweenExpirationCheck().toMillis());
        }

        return cf;
    }
}
