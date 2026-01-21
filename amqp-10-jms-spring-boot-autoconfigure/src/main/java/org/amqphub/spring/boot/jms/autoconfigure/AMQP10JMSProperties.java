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

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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

    @NestedConfigurationProperty
    private final DeserializationPolicy deserializationPolicy = new DeserializationPolicy();

    @NestedConfigurationProperty
    private final AMQP10JMSJmsPoolConnectionFactoryProperties pool = new AMQP10JMSJmsPoolConnectionFactoryProperties();

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

    public AMQP10JMSJmsPoolConnectionFactoryProperties getPool() {
        return pool;
    }

    public static class DeserializationPolicy {

        /**
         * Allow-list of classes or packages.
         */
        private List<String> allowList;

        /**
         * Deny-list of classes or packages. Deny-List overrides the Allow-list, entries that could match both are
         * counted as denied.
         */
        private List<String> denyList;

        public List<String> getAllowList() {
            return this.allowList;
        }

        public void setAllowList(List<String> allowList) {
            this.allowList = allowList;
        }

        public List<String> getDenyList() {
            return this.denyList;
        }

        public void setDenyList(List<String> denyList) {
            this.denyList = denyList;
        }
    }
}
