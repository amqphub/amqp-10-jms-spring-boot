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
package org.amqphub.spring.boot.jms.example;

import org.amqphub.spring.boot.jms.autoconfigure.AMQP10JMSConnectionFactoryCustomizer;
import org.apache.qpid.jms.policy.JmsDefaultMessageIDPolicy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * Simple Hello World example that sends and receives a message
 */
@SpringBootApplication
@EnableJms
public class AMQP10JMSSpringBootCustomConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(AMQP10JMSSpringBootCustomConfiguration.class, args);
    }

    /**
     * Uses a configuration annotation to define an AMQP10JMSConnectionFactoryCustomizer
     * override that configures the Qpid JMS ConnectionFactory used by the starter to match
     * requirements of the user.  In this case the login credentials are set and the client
     * is configured to use UUID's as the JMS Message ID values.
     */
    @Configuration
    protected static class CustomAMQP10JMSClientConfiguration {

        @Bean
        public AMQP10JMSConnectionFactoryCustomizer myAMQP10Configuration() {
            return (factory) -> {
                factory.setUsername("admin");
                factory.setPassword("admin");

                ((JmsDefaultMessageIDPolicy) factory.getMessageIDPolicy()).setMessageIDType("UUID");
            };
        }
    }
}
