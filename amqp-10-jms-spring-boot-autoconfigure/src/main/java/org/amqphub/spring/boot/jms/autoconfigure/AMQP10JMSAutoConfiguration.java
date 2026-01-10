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
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration;
import org.springframework.boot.jms.autoconfigure.JmsProperties;
import org.springframework.boot.jms.autoconfigure.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import jakarta.jms.ConnectionFactory;

/**
 * Auto Configuration class for the AMQP 1.0 JMS client allowing for the addition
 * of connection pooling based on configuration. The auto configuration creates a
 * {@link ConnectionFactory} that provides Qpid JMS based connections to the application
 * configured from {@link AMQP10JMSProperties} if pooling is enabled the Qpid JMS factory
 * is wrapped in a {@link ConnectionFactory} that pooled JMS connection resources.
 *
 * @see AMQP10JMSProperties
 */
@Configuration
@AutoConfiguration(before = JmsAutoConfiguration.class, after = JndiConnectionFactoryAutoConfiguration.class)
@ConditionalOnMissingBean(ConnectionFactory.class)
@ConditionalOnClass({ConnectionFactory.class, JmsConnectionFactory.class})
@EnableConfigurationProperties({AMQP10JMSProperties.class, JmsProperties.class})
@Import(AMQP10JMSConnectionFactoryConfiguration.class)
public class AMQP10JMSAutoConfiguration {

}
