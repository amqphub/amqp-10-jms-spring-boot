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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test for AMQP 1.0 JMS Properties object.
 */
public class AMQP10JMSPropertiesTest {

    private static final String DEFAULT_AMQP_REMOTE_URL = "amqp://localhost:5672";

    private final AMQP10JMSProperties properties = new AMQP10JMSProperties();

    @Test
    public void testAMQPDefaultRemoteURL() {
        assertEquals(DEFAULT_AMQP_REMOTE_URL, properties.getRemoteUrl());
    }

    @Test
    public void testBlankAuthenticationCerdentialsByDefault() {
        assertNull(properties.getUsername());
        assertNull(properties.getPassword());
    }

    @Test
    public void testNoClientIdSetByDefault() {
        assertNull(properties.getClientId());
    }

    @Test
    public void testPoolDisabledByDefault() {
        assertFalse(properties.getPool().isEnabled());
    }

    @Test
    public void testPoolOptionsDefaults() {
        assertFalse(properties.getPool().isEnabled());
        assertTrue(properties.getPool().isBlockIfSessionPoolIsFull());
        assertTrue(properties.getPool().isUseAnonymousProducers());
        assertFalse(properties.getPool().isUseProviderJMSContext());

        assertEquals(1, properties.getPool().getMaxConnections());
        assertEquals(500, properties.getPool().getMaxSessionsPerConnection());
        assertEquals(0, properties.getPool().getExplicitProducerCacheSize());

        assertEquals(30000, properties.getPool().getConnectionIdleTimeout().toMillis());
        assertEquals(-1, properties.getPool().getConnectionCheckInterval().toMillis());
        assertEquals(-1, properties.getPool().getBlockIfSessionPoolIsFullTimeout().toMillis());
    }
}
