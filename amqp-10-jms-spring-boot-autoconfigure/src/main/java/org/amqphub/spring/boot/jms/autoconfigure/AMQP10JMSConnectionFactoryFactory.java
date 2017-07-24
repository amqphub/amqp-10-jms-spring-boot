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

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.policy.JmsDefaultDeserializationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Builder of AMQP 1.0 ConnectionFactory instances.
 */
public class AMQP10JMSConnectionFactoryFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AMQP10JMSConnectionFactoryFactory.class);

    private static final String DEFAULT_REMOTE_URL = "amqp://localhost:5672";

    private final AMQP10JMSProperties properties;

    /**
     * Creates a new QpidJMSConnectionFactoryFactory instance
     *
     * @param properties
     *      The QpidJMSProperties to use when building new factories.
     */
    public AMQP10JMSConnectionFactoryFactory(AMQP10JMSProperties properties) {
        Assert.notNull(properties, "Properties must not be null");
        this.properties = properties;
    }

    /**
     * Creates and returns a JmsConnectionFactory instance using the current
     * configuration to prepare the factory for use.
     *
     * @param factoryClass
     *      The type of JmsConnectionFactory to create.
     *
     * @return a newly created and configured JmsConnectionFactory instance.
     */
    public JmsConnectionFactory createConnectionFactory(Class<JmsConnectionFactory> factoryClass) {
        try {
            JmsConnectionFactory factory = new JmsConnectionFactory();

            factory.setRemoteURI(getRemoteURI());

            // Override the URI options with configuration values, but only if
            // the value is actually set.

            if (StringUtils.hasLength(properties.getUsername())) {
                factory.setUsername(properties.getUsername());
            }

            if (StringUtils.hasLength(properties.getPassword())) {
                factory.setPassword(properties.getPassword());
            }

            if (StringUtils.hasLength(properties.getClientId())) {
                factory.setClientID(properties.getClientId());
            }

            if (properties.isReceiveLocalOnly() != null) {
                factory.setReceiveLocalOnly(properties.isReceiveLocalOnly());
            }

            if (properties.isReceiveNoWaitLocalOnly() != null) {
                factory.setReceiveNoWaitLocalOnly(properties.isReceiveNoWaitLocalOnly());
            }

            configureDeserializationPolicy(properties, factory);

            return factory;
        } catch (Exception ex) {
            LOG.error("Exception while createing the AMQP 1.0 JMS Connection Factory.", ex);
            throw new IllegalStateException("Failed to create the AMQP 1.0 JMS ConnectionFactory, " +
                "make sure the client Jar is on the Classpath.", ex);
        }
    }

    public String getRemoteURI() {
        if (StringUtils.hasLength(properties.getRemoteURL())) {
            return properties.getRemoteURL();
        } else {
            return DEFAULT_REMOTE_URL;
        }
    }

    private void configureDeserializationPolicy(AMQP10JMSProperties properties, JmsConnectionFactory factory) {
        JmsDefaultDeserializationPolicy deserializationPolicy =
            (JmsDefaultDeserializationPolicy) factory.getDeserializationPolicy();

        if (StringUtils.hasLength(properties.getDeserializationPolicy().getWhiteList())) {
            deserializationPolicy.setWhiteList(properties.getDeserializationPolicy().getWhiteList());
        }

        if (StringUtils.hasLength(properties.getDeserializationPolicy().getBlackList())) {
            deserializationPolicy.setBlackList(properties.getDeserializationPolicy().getBlackList());
        }
    }
}
