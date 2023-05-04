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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldMessageProducer implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldMessageProducer.class);

    @Autowired
    public JmsTemplate jmsTemplate;

    @Override
    public void run(String... strings) throws Exception {
        final String messageText = "Hello World";
        LOG.info("============= Sending: " + messageText);
        sendMessage(messageText);
    }

    public void sendMessage(String payload) {
        this.jmsTemplate.convertAndSend("example", payload);
    }
}
