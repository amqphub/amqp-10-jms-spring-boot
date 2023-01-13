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

import jakarta.jms.ConnectionFactory;

/**
 * Functional interface that allows for user overrides of the configured options
 * for the Qpid JMS {@link JmsConnectionFactory} to be created by this starter.
 */
@FunctionalInterface
public interface AMQP10JMSConnectionFactoryCustomizer {

    /**
     * Customize the Qpid JMS {@link JmsConnectionFactory}.
     *
     * @param factory
     *      the Qpid JMS {@link ConnectionFactory} to customize
     */
    void customize(JmsConnectionFactory factory);

}
