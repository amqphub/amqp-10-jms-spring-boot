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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the AMQP 1.0 JMS client
 */
@ConfigurationProperties(prefix = "amqphub.amqp10jms")
public class AMQP10JMSProperties {

    private String remoteURL;
    private String username;
    private String password;
    private String clientId;

    private Boolean receiveLocalOnly;
    private Boolean receiveNoWaitLocalOnly;

    private final DeserializationPolicy deserializationPolicy = new DeserializationPolicy();

    public String getRemoteURL() {
        return remoteURL;
    }

    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
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

    public Boolean isReceiveLocalOnly() {
        return receiveLocalOnly;
    }

    public void setReceiveLocalOnly(Boolean receiveLocalOnly) {
        this.receiveLocalOnly = receiveLocalOnly;
    }

    public Boolean isReceiveNoWaitLocalOnly() {
        return receiveNoWaitLocalOnly;
    }

    public void setReceiveNoWaitLocalOnly(Boolean receiveNoWaitLocalOnly) {
        this.receiveNoWaitLocalOnly = receiveNoWaitLocalOnly;
    }

    public DeserializationPolicy getDeserializationPolicy() {
        return deserializationPolicy;
    }

    public static class DeserializationPolicy {

        private String whiteList;
        private String blackList;

        public String getWhiteList() {
            return whiteList;
        }

        public void setWhiteList(String whiteList) {
            this.whiteList = whiteList;
        }

        public String getBlackList() {
            return blackList;
        }

        public void setBlackList(String blackList) {
            this.blackList = blackList;
        }
    }
}
