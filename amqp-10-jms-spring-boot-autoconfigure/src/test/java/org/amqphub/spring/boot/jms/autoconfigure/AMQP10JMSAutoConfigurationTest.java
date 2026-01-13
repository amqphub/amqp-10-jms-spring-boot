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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.policy.JmsDefaultDeserializationPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import jakarta.jms.ConnectionFactory;

/**
 * Test that AMQP 1.0 JMS Auto Configuration works.
 */
public class AMQP10JMSAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(AMQP10JMSAutoConfiguration.class, JmsAutoConfiguration.class));

    @Test
    public void testConnectionFactoryIsCachedByDefault() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class).run((context) -> {
            assertThat(context).hasSingleBean(ConnectionFactory.class).hasSingleBean(CachingConnectionFactory.class)
                    .hasBean("jmsConnectionFactory");
            CachingConnectionFactory connectionFactory = context.getBean(CachingConnectionFactory.class);
            assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);
            assertThat(connectionFactory.getTargetConnectionFactory()).isInstanceOf(JmsConnectionFactory.class);
            assertThat(connectionFactory.isCacheConsumers()).isFalse();
            assertThat(connectionFactory.isCacheProducers()).isTrue();
            assertThat(connectionFactory.getSessionCacheSize()).isOne();
            assertThat(connectionFactory.getTargetConnectionFactory() instanceof JmsConnectionFactory);
        });
    }

    @Test
    public void testDefaultsToLocalURI() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class).run((context) -> {
            assertThat(context).hasSingleBean(ConnectionFactory.class).hasSingleBean(CachingConnectionFactory.class)
                    .hasBean("jmsConnectionFactory");
            CachingConnectionFactory connectionFactory = context.getBean(CachingConnectionFactory.class);
            assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);

            JmsConnectionFactory qpidJmsFactory = (JmsConnectionFactory) connectionFactory.getTargetConnectionFactory();

            assertEquals("amqp://localhost:5672", qpidJmsFactory.getRemoteURI());
            assertNull(qpidJmsFactory.getUsername());
            assertNull(qpidJmsFactory.getPassword());
        });
    }

    @Test
    public void testConnectionFactoryCachedCanBeDisabled() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
            .withPropertyValues("spring.jms.cache.enabled=false").run((context) -> {

            assertThat(context).hasSingleBean(ConnectionFactory.class).hasSingleBean(AMQP10JMSAutoConfiguration.class)
                    .hasBean("jmsConnectionFactory");
            ConnectionFactory connectionFactory = context.getBean(ConnectionFactory.class);
            assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);

            assertThat(connectionFactory instanceof JmsConnectionFactory);
        });
    }

    @Test
    public void testCustomConnectionFactorySettings() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
            .withPropertyValues(
                "spring.jms.cache.enabled=false",
                "amqphub.amqp10jms.remote-url=amqp://127.0.0.1:5672",
                "amqphub.amqp10jms.username=foo",
                "amqphub.amqp10jms.password=bar").run((context) -> {

            assertThat(context).hasSingleBean(ConnectionFactory.class).hasSingleBean(AMQP10JMSAutoConfiguration.class)
                    .hasBean("jmsConnectionFactory");
            ConnectionFactory connectionFactory = context.getBean(ConnectionFactory.class);
            assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);

            assertThat(connectionFactory instanceof JmsConnectionFactory);

            JmsConnectionFactory qpidJmsFactory = (JmsConnectionFactory) connectionFactory;
            assertEquals("amqp://127.0.0.1:5672", qpidJmsFactory.getRemoteURI());
            assertEquals("foo", qpidJmsFactory.getUsername());
            assertEquals("bar", qpidJmsFactory.getPassword());
        });
    }

    @Test
    public void testReceiveLocalOnlyOptionsAppliedFromEnv() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
            .withPropertyValues(
                "spring.jms.cache.enabled=false",
                "amqphub.amqp10jms.receiveLocalOnly=true",
                "amqphub.amqp10jms.receiveNoWaitLocalOnly=true").run((context) -> {

            assertThat(context).hasSingleBean(ConnectionFactory.class).hasSingleBean(AMQP10JMSAutoConfiguration.class)
                    .hasBean("jmsConnectionFactory");
            ConnectionFactory connectionFactory = context.getBean(ConnectionFactory.class);
            assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);

            assertThat(connectionFactory instanceof JmsConnectionFactory);

            JmsConnectionFactory qpidJmsFactory = (JmsConnectionFactory) connectionFactory;
            assertTrue(qpidJmsFactory.isReceiveLocalOnly());
            assertTrue(qpidJmsFactory.isReceiveNoWaitLocalOnly());
        });
    }

    @Test
    public void testReceiveLocalOnlyOptionsAppliedFromEnvOverridesURI() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
            .withPropertyValues(
                "spring.jms.cache.enabled=false",
                "amqphub.amqp10jms.remote-url=amqp://127.0.0.1:5672" +
                    "?jms.receiveLocalOnly=false&jms.receiveNoWaitLocalOnly=false",
                "amqphub.amqp10jms.receiveLocalOnly=true",
                "amqphub.amqp10jms.receiveNoWaitLocalOnly=true").run((context) -> {

            assertThat(context).hasSingleBean(ConnectionFactory.class).hasSingleBean(AMQP10JMSAutoConfiguration.class)
                    .hasBean("jmsConnectionFactory");
            ConnectionFactory connectionFactory = context.getBean(ConnectionFactory.class);
            assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);

            assertThat(connectionFactory instanceof JmsConnectionFactory);

            JmsConnectionFactory qpidJmsFactory = (JmsConnectionFactory) connectionFactory;
            assertTrue(qpidJmsFactory.isReceiveLocalOnly());
            assertTrue(qpidJmsFactory.isReceiveNoWaitLocalOnly());
        });
    }

    @Test
    public void testAllowListDefaultToNonEmpty() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
            .withPropertyValues("spring.jms.cache.enabled=false",
                                "amqphub.amqp10jms.remote-url=amqp://127.0.0.1:5672"
                ).run((context) -> {

                JmsConnectionFactory factory = (JmsConnectionFactory) context.getBean(ConnectionFactory.class);
                JmsDefaultDeserializationPolicy policy = (JmsDefaultDeserializationPolicy) factory.getDeserializationPolicy();

                assertThat(policy.getAllowList().isEmpty()).isFalse();
            });
    }

    @Test
    public void testDenyListDefaultToEmpty() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
            .withPropertyValues("spring.jms.cache.enabled=false",
                                "amqphub.amqp10jms.remote-url=amqp://127.0.0.1:5672"
                ).run((context) -> {

                JmsConnectionFactory factory = (JmsConnectionFactory) context.getBean(ConnectionFactory.class);
                JmsDefaultDeserializationPolicy policy = (JmsDefaultDeserializationPolicy) factory.getDeserializationPolicy();

                assertThat(policy.getDenyList().isEmpty()).isTrue();
            });
    }

    @Test
    public void testDeserializationPolicyValuesAreApplied() {
        this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
            .withPropertyValues("spring.jms.cache.enabled=false",
                                "amqphub.amqp10jms.remote-url=amqp://127.0.0.1:5672",
                                "amqphub.amqp10jms.deserializationPolicy.allowList=org.apache.qpid.proton.*",
                                "amqphub.amqp10jms.deserializationPolicy.denyList=org.apache.activemq..*"
                ).run((context) -> {

                JmsConnectionFactory factory = (JmsConnectionFactory) context.getBean(ConnectionFactory.class);
                JmsDefaultDeserializationPolicy policy = (JmsDefaultDeserializationPolicy) factory.getDeserializationPolicy();

                assertEquals("org.apache.qpid.proton.*", policy.getAllowList());
                assertEquals("org.apache.activemq..*", policy.getDenyList());
            });
    }

    @Test
    public void testCreateWithCustomArtemisConfiguration() {
        this.contextRunner.withUserConfiguration(CustomAMQP10JMSConfiguration.class)
            .run((context) -> {
                CachingConnectionFactory connectionFactory = context.getBean(CachingConnectionFactory.class);
                assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);

                JmsConnectionFactory qpidJmsFactory = (JmsConnectionFactory) connectionFactory.getTargetConnectionFactory();

                assertThat(qpidJmsFactory.getUsername()).isEqualTo("customizedUser");
                assertThat(qpidJmsFactory.getPassword()).isEqualTo("customizedPass");
            });
    }

    @Test
    public void testCreateWithMultipleCustomArtemisConfiguration() {
        this.contextRunner.withUserConfiguration(MultipleCustomAMQP10JMSConfigurations.class)
            .run((context) -> {
                CachingConnectionFactory connectionFactory = context.getBean(CachingConnectionFactory.class);
                assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);

                JmsConnectionFactory qpidJmsFactory = (JmsConnectionFactory) connectionFactory.getTargetConnectionFactory();

                assertThat(qpidJmsFactory.getUsername()).isEqualTo("customizedUser");
                assertThat(qpidJmsFactory.getPassword()).isEqualTo("customizedPass");
            });
    }

    @Configuration(proxyBeanMethods = false)
    static class EmptyConfiguration {

    }

    @Configuration(proxyBeanMethods = false)
    static class CustomAMQP10JMSConfiguration {

        @Bean
        AMQP10JMSConnectionFactoryCustomizer configurationCustomizer() {
            return (configuration) -> {
                configuration.setPassword("customizedPass");
                configuration.setUsername("customizedUser");
            };
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class MultipleCustomAMQP10JMSConfigurations {

        @Bean(name = "configurationCustomizer1")
        AMQP10JMSConnectionFactoryCustomizer configurationCustomizer1() {
            return (configuration) -> {
                configuration.setUsername("customizedUser");
            };
        }

        @Bean(name = "configurationCustomizer2")
        AMQP10JMSConnectionFactoryCustomizer configurationCustomizer2() {
            return (configuration) -> {
                configuration.setPassword("customizedPass");
            };
        }
    }
}
