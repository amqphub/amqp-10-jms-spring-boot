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

import static org.junit.Assert.assertEquals;

import javax.jms.JMSException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Simple Hello World example that sends and receives a message using both the
 * Hello World Command Line instance and a manual call to show either can work.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PooledConnectionExampleTest {

    private static BrokerService brokerService;

    @Autowired
    public PooledMessageConsumer consumer;

    @Autowired
    public PooledMessageProducer producer;

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUp() throws Exception {
        brokerService = new BrokerService();

        brokerService.addConnector("amqp://localhost:5672");
        brokerService.setPersistent(false);
        brokerService.getManagementContext().setCreateConnector(false);

        brokerService.start();
        brokerService.waitUntilStarted();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        brokerService.stop();
        brokerService.waitUntilStopped();
    }

    @Test
    public void testMessageIsSent() throws Exception {
        producer.sendMessage("Hello: " + name.getMethodName());

        // Should have our send plus the one's sent by the run of MessageProducer by Spring
        QueueViewMBean queueView = getProxyToQueue("example");
        assertEquals(20, queueView.getEnqueueCount());

        BrokerViewMBean brokerView = getProxyToBroker();
        assertEquals(1, brokerView.getCurrentConnectionsCount());
    }

    protected BrokerViewMBean getProxyToBroker() throws MalformedObjectNameException, JMSException {
        ObjectName brokerViewMBean = new ObjectName(
            "org.apache.activemq:type=Broker,brokerName=localhost");
        BrokerViewMBean proxy = (BrokerViewMBean) brokerService.getManagementContext()
                .newProxyInstance(brokerViewMBean, BrokerViewMBean.class, true);
        return proxy;
    }

    protected QueueViewMBean getProxyToQueue(String name) throws MalformedObjectNameException, JMSException {
        ObjectName queueViewMBeanName = new ObjectName("org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName="+name);
        QueueViewMBean proxy = (QueueViewMBean) brokerService.getManagementContext()
                .newProxyInstance(queueViewMBeanName, QueueViewMBean.class, true);
        return proxy;
    }
}
