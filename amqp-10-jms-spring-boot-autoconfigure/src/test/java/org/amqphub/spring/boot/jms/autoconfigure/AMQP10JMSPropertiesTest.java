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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.policy.JmsDefaultDeserializationPolicy;
import org.junit.Test;

import java.util.Collections;

/**
 * Test for AMQP 1.0 JMS Properties object.
 */
public class AMQP10JMSPropertiesTest {

    private final AMQP10JMSProperties properties = new AMQP10JMSProperties();

    @Test
    public void testWhiteListDefaultToEmpty() {
        JmsConnectionFactory factory = new AMQP10JMSConnectionFactoryFactory(this.properties).createConnectionFactory(JmsConnectionFactory.class);

        JmsDefaultDeserializationPolicy policy = (JmsDefaultDeserializationPolicy) factory.getDeserializationPolicy();

        assertEquals(1, policy.getWhiteList().length());
    }

    @Test
    public void testLocalOnlyReceiveOptions() {
        this.properties.setReceiveLocalOnly(true);
        this.properties.setReceiveNoWaitLocalOnly(true);

        JmsConnectionFactory factory = new AMQP10JMSConnectionFactoryFactory(this.properties).createConnectionFactory(JmsConnectionFactory.class);

        assertTrue(factory.isReceiveLocalOnly());
        assertTrue(factory.isReceiveNoWaitLocalOnly());
    }

    @Test
    public void testBlackListDefaultToEmpty() {
        JmsConnectionFactory factory = new AMQP10JMSConnectionFactoryFactory(this.properties).createConnectionFactory(JmsConnectionFactory.class);

        JmsDefaultDeserializationPolicy policy = (JmsDefaultDeserializationPolicy) factory.getDeserializationPolicy();

        assertEquals(0, policy.getBlackList().length());
    }

    @Test
    public void testDeserializationPolicyValuesAreApplied() {
        this.properties.getDeserializationPolicy().setWhiteList(Collections.singletonList("org.apache.qpid.proton.*"));
        this.properties.getDeserializationPolicy().setBlackList(Collections.singletonList("org.apache.activemq..*"));

        JmsConnectionFactory factory = new AMQP10JMSConnectionFactoryFactory(this.properties).createConnectionFactory(JmsConnectionFactory.class);

        JmsDefaultDeserializationPolicy policy = (JmsDefaultDeserializationPolicy) factory.getDeserializationPolicy();

        assertEquals("org.apache.qpid.proton.*", policy.getWhiteList());
        assertEquals("org.apache.activemq..*", policy.getBlackList());
    }
}
