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
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Builder of AMQP 1.0 ConnectionFactory instances.
 */
public class AMQP10JMSConnectionFactoryFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AMQP10JMSConnectionFactoryFactory.class);

    private final AMQP10JMSProperties properties;

    private final ObjectProvider<AMQP10JMSConnectionFactoryCustomizer> factoryCustomizers;

    /**
     * Creates a new QpidJMSConnectionFactoryFactory instance
     *
     * @param properties
     *      The QpidJMSProperties to use when building new factories.
     * @param factoryCustomizers
     *      Optional list of customizers used to let users override configuration settings.
     */
    public AMQP10JMSConnectionFactoryFactory(AMQP10JMSProperties properties, ObjectProvider<AMQP10JMSConnectionFactoryCustomizer> factoryCustomizers) {
        Assert.notNull(properties, "Properties must not be null");
        this.properties = properties;
        this.factoryCustomizers = factoryCustomizers;
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
            JmsConnectionFactory factory = factoryClass.getConstructor().newInstance();

            factory.setRemoteURI(properties.getRemoteUrl());

            if (StringUtils.hasLength(properties.getUsername())) {
                factory.setUsername(properties.getUsername());
            }

            if (StringUtils.hasLength(properties.getPassword())) {
                factory.setPassword(properties.getPassword());
            }

            if (StringUtils.hasLength(properties.getClientId())) {
                factory.setClientID(properties.getClientId());
            }

            factory.setReceiveLocalOnly(properties.isReceiveLocalOnly());

            factory.setReceiveNoWaitLocalOnly(properties.isReceiveNoWaitLocalOnly());

            configureDeserializationPolicy(properties, factory);

            // User callback point to allow for control over Qpid JMS ConnectionFactory
            // configuration that overrides the properties previously set
            customizeFactoryConfiguration(factory);

            return factory;
        } catch (Exception ex) {
            LOG.error("Exception while createing the AMQP 1.0 JMS Connection Factory.", ex);
            throw new IllegalStateException("Failed to create the AMQP 1.0 JMS ConnectionFactory, " +
                "make sure the client Jar is on the Classpath.", ex);
        }
    }

    private void configureDeserializationPolicy(AMQP10JMSProperties properties, JmsConnectionFactory factory) {
        JmsDefaultDeserializationPolicy deserializationPolicy =
            (JmsDefaultDeserializationPolicy) factory.getDeserializationPolicy();

        if (!ObjectUtils.isEmpty(properties.getDeserializationPolicy().getAllowList())) {
            deserializationPolicy.setAllowList(StringUtils.collectionToCommaDelimitedString(
                properties.getDeserializationPolicy().getAllowList()));
        }

        if (!ObjectUtils.isEmpty(properties.getDeserializationPolicy().getDenyList())) {
            deserializationPolicy.setDenyList(StringUtils.collectionToCommaDelimitedString(
                properties.getDeserializationPolicy().getDenyList()));
        }
    }

    private void customizeFactoryConfiguration(JmsConnectionFactory connectionFactory) {
        factoryCustomizers.orderedStream().forEach((customizer) -> customizer.customize(connectionFactory));
    }
}
