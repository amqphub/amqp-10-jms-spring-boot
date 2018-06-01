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
package org.amqphub.spring.boot.example;

import javax.jms.ConnectionFactory;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;

/**
 * Simple example showing how to use the Spring {@link SimpleJmsListenerContainerFactory}
 * instead of the default {@link DefaultJmsListenerContainerFactory}
 */
@SpringBootApplication
@EnableJms
public class SimpleJmsListenerContainerFactoryExample {

    public static void main(String[] args) {
        SpringApplication.run(SimpleJmsListenerContainerFactoryExample.class, args);
    }

    @Bean(name = "QpidJMSContainerFactory")
    public JmsListenerContainerFactory<?> queue(ConnectionFactory cf) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        JmsConnectionFactory qpidCF = (JmsConnectionFactory) cf;

        factory.setConnectionFactory(qpidCF);
        factory.setSessionTransacted(false);

        return factory;
    }
}
