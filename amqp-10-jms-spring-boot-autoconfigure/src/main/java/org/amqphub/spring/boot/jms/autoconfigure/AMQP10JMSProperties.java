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

import java.time.Duration;
import java.util.List;

import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the AMQP 1.0 JMS client
 */
@ConfigurationProperties(prefix = "amqphub.amqp10jms")
public class AMQP10JMSProperties {

    /**
     * The URL of the remote peer the AMQP client should connect to.
     */
    private String remoteUrl = "amqp://localhost:5672";

    /**
     * AMQP broker user name.
     */
    private String username;

    /**
     * Login password of the AMQP broker.
     */
    private String password;

    /**
     * JMS clientID to use for connections. A clientID can only be used by one Connection at a time, so setting it
     * will restrict the ConnectionFactory to creating a single open Connection at a time.
     */
    private String clientId;

    /**
     * Whether the client only checks its local message buffer when using receive calls with a timeout.
     */
    private boolean receiveLocalOnly = false;

    /**
     * Whether the client only checks its local message buffer when using receiveNoWait calls.
     */
    private boolean receiveNoWaitLocalOnly = false;

    private final DeserializationPolicy deserializationPolicy = new DeserializationPolicy();

    private Pool pool = new Pool();

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isReceiveLocalOnly() {
        return receiveLocalOnly;
    }

    public void setReceiveLocalOnly(boolean receiveLocalOnly) {
        this.receiveLocalOnly = receiveLocalOnly;
    }

    public boolean isReceiveNoWaitLocalOnly() {
        return receiveNoWaitLocalOnly;
    }

    public void setReceiveNoWaitLocalOnly(boolean receiveNoWaitLocalOnly) {
        this.receiveNoWaitLocalOnly = receiveNoWaitLocalOnly;
    }

    public DeserializationPolicy getDeserializationPolicy() {
        return deserializationPolicy;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public static class DeserializationPolicy {

        /**
         * Whitelist of classes or packages.
         */
        private List<String> whiteList;

        /**
         * Blacklist of classes or packages. Blacklist overrides the whitelist, entries that could match both are
         * counted as blacklisted.
         */
        private List<String> blackList;

        public List<String> getWhiteList() {
            return this.whiteList;
        }

        public void setWhiteList(List<String> whiteList) {
            this.whiteList = whiteList;
        }

        public List<String> getBlackList() {
            return this.blackList;
        }

        public void setBlackList(List<String> blackList) {
            this.blackList = blackList;
        }
    }

    public static class Pool {

        /**
         * Whether a JmsPoolConnectionFactory should be created and used to wrap the
         * Qpid JMS ConnectionFactory.
         */
        private boolean enabled;

        /**
         * Whether to block when a Session is requested and the Session pool is full. Set it to
         * false to throw a "JMSException" instead.
         */
        private boolean blockIfSessionPoolIsFull = true;

        /**
         * Blocking period before throwing an exception if the Session pool is still full and the
         * blockIfSessionPoolIsFull configuration option is set to 'true'
         */
        private Duration blockIfSessionPoolIsFullTimeout = Duration.ofMillis(-1);

        /**
         * Connection idle timeout for connections that are not currently in use.
         */
        private Duration connectionIdleTimeout = Duration.ofSeconds(30);

        /**
         * Maximum number of pooled connections.
         */
        private int maxConnections = 1;

        /**
         * Maximum number of sessions allowed for each connection in the pool.
         */
        private int maxSessionsPerConnection = 500;

        /**
         * Time to sleep between runs of the connection check thread whcih will only
         * run if the configuration value is non-negative.
         */
        private Duration connectionCheckInterval = Duration.ofMillis(-1);

        /**
         * Whether to use only one anonymous "MessageProducer" instance. Set it to false
         * to create one "MessageProducer" every time one is required.
         */
        private boolean useAnonymousProducers = true;

        /**
         * When the useAnonymousProducers option is disabled this option controls whether a
         * pooled session will cache some number of explicit JMS producers in an LRUCache.
         */
        private int explicitProducerCacheSize = 0;

        /**
         * Controls whether the pool will use a generic JMSContext that wraps a pooled Connection or
         * uses the provider JMS ConnectionFactory to directly create JMSContext instances.  The generic
         * JMSContext object cannot fully implement all methods of the simplified API and must be disabled
         * in the case where those methods are needed.
         */
        private boolean useProviderJMSContext = false;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isBlockIfSessionPoolIsFull() {
            return this.blockIfSessionPoolIsFull;
        }

        public void setBlockIfSessionPoolIsFull(boolean blockIfSessionPoolIsFull) {
            this.blockIfSessionPoolIsFull = blockIfSessionPoolIsFull;
        }

        public Duration getBlockIfSessionPoolIsFullTimeout() {
            return this.blockIfSessionPoolIsFullTimeout;
        }

        public void setBlockIfSessionPoolIsFullTimeout(long blockIfSessionPoolIsFullTimeout) {
            this.blockIfSessionPoolIsFullTimeout = Duration.ofMillis(blockIfSessionPoolIsFullTimeout);
        }

        public Duration getConnectionIdleTimeout() {
            return this.connectionIdleTimeout;
        }

        public void setConnectionIdleTimeout(long connectionIdleTimeout) {
            this.connectionIdleTimeout = Duration.ofMillis(connectionIdleTimeout);
        }

        public int getMaxConnections() {
            return this.maxConnections;
        }

        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }

        public int getMaxSessionsPerConnection() {
            return this.maxSessionsPerConnection;
        }

        public void setMaxSessionsPerConnection(int maxSessionPerConnection) {
            this.maxSessionsPerConnection = maxSessionPerConnection;
        }

        public Duration getConnectionCheckInterval() {
            return this.connectionCheckInterval;
        }

        public void setConnectionCheckInterval(long connectionCheckInterval) {
            this.connectionCheckInterval = Duration.ofMillis(connectionCheckInterval);
        }

        public boolean isUseAnonymousProducers() {
            return this.useAnonymousProducers;
        }

        public void setUseAnonymousProducers(boolean useAnonymousProducers) {
            this.useAnonymousProducers = useAnonymousProducers;
        }

        public boolean isUseProviderJMSContext() {
            return useProviderJMSContext;
        }

        public void setUseProviderJMSContext(boolean useProviderJMSContext) {
            this.useProviderJMSContext = useProviderJMSContext;
        }

        public int getExplicitProducerCacheSize() {
            return explicitProducerCacheSize;
        }

        public void setExplicitProducerCacheSize(int explicitProducerCacheSize) {
            this.explicitProducerCacheSize = explicitProducerCacheSize;
        }

        public void configurePooledFactory(JmsPoolConnectionFactory factory) {
            factory.setBlockIfSessionPoolIsFull(isBlockIfSessionPoolIsFull());
            if (getBlockIfSessionPoolIsFullTimeout() != null) {
                factory.setBlockIfSessionPoolIsFullTimeout(getBlockIfSessionPoolIsFullTimeout().toMillis());
            }
            if (getConnectionCheckInterval() != null) {
                factory.setConnectionCheckInterval(getConnectionCheckInterval().toMillis());
            }
            if (getConnectionIdleTimeout() != null) {
                factory.setConnectionIdleTimeout((int) getConnectionIdleTimeout().toMillis());
            }
            factory.setExplicitProducerCacheSize(getExplicitProducerCacheSize());
            factory.setMaxConnections(getMaxConnections());
            factory.setMaxSessionsPerConnection(getMaxSessionsPerConnection());
            factory.setUseAnonymousProducers(isUseAnonymousProducers());
            factory.setUseProviderJMSContext(isUseProviderJMSContext());
        }
    }
}
