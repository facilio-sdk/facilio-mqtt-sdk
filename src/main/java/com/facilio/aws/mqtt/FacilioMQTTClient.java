package com.facilio.aws.mqtt;

import com.amazonaws.services.iot.client.*;
import com.amazonaws.services.iot.client.core.AwsIotRuntimeException;
import com.facilio.util.FacilioProperties;
import com.facilio.util.KeyStorePasswordPair;
import com.facilio.util.PropertyKeys;

import java.security.KeyStore;

public class FacilioMQTTClient extends AWSIotMqttClient {

    private static FacilioMQTTClient client = null;
    private static KeyStorePasswordPair pair = null;

    private FacilioMQTTClient(String clientEndpoint, String clientId, KeyStore keyStore, String keyPassword) {
        super(clientEndpoint, clientId, keyStore, keyPassword);
    }

    private FacilioMQTTClient(String clientEndpoint, String clientId, String awsAccessKeyId, String awsSecretAccessKey) {
        super(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey);
    }

    private FacilioMQTTClient(String clientEndpoint, String clientId, String awsAccessKeyId, String awsSecretAccessKey, String sessionToken) {
        super(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, sessionToken);
    }

    public static FacilioMQTTClient getMQTTClient() {
        return getMQTTClient(FacilioProperties.getProperty(PropertyKeys.END_POINT), FacilioProperties.getProperty(PropertyKeys.CLIENT_ID));
    }

    private static FacilioMQTTClient getMQTTClient(String clientEndpoint, String clientId) {
        if(pair == null ) {
            if(FacilioProperties.getProperty(PropertyKeys.KEY_FILE) != null && FacilioProperties.getProperty(PropertyKeys.CERT_FILE) != null) {
                pair = KeyStorePasswordPair.getKeyStorePasswordPair(FacilioProperties.getFacilioHome() + FacilioProperties.getProperty(PropertyKeys.CERT_FILE),
                        FacilioProperties.getFacilioHome() + FacilioProperties.getProperty(PropertyKeys.KEY_FILE));
            }
        }
        if(pair != null) {
            if(client == null) {
                client = new FacilioMQTTClient(clientEndpoint, clientId, pair.getKeyStore(), pair.getKeyPassword());
            }
        }
        return client;
    }

    public void publish(String topic, String payload) {
        publish(new Message(topic, AWSIotQos.QOS0, payload));
    }

    @Override
    public void publish(AWSIotMessage message) {
        if ( ! (client.getConnectionStatus() == AWSIotConnectionStatus.CONNECTED)) {
            connectClient();
        }
        try {
            super.publish(message, 60000);
        } catch (AWSIotException | AwsIotRuntimeException e) {
            message.onFailure();
            e.printStackTrace();
            if ( ! (client.getConnectionStatus() == AWSIotConnectionStatus.CONNECTED)) {
                connectClient();
            }
        }
    }

    @Override
    public void onConnectionFailure() {
        super.onConnectionFailure();
        System.out.println("Connection failure");
    }

    @Override
    public void onConnectionClosed() {
        super.onConnectionClosed();
        System.out.println("Connection closed");
    }

    private void connectClient() {
        try {
            client.connect();
            if(client.getConnectionStatus() == AWSIotConnectionStatus.CONNECTED) {
                System.out.println("Connection established");
            } else {
                System.out.println("Connection failed");
            }
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getMaxConnectionRetries() {
        return super.getMaxConnectionRetries();
    }

    @Override
    public void setMaxConnectionRetries(int maxConnectionRetries) {
        super.setMaxConnectionRetries(maxConnectionRetries);
    }

    @Override
    public int getBaseRetryDelay() {
        return super.getBaseRetryDelay();
    }

    @Override
    public void setBaseRetryDelay(int baseRetryDelay) {
        super.setBaseRetryDelay(baseRetryDelay);
    }

    @Override
    public int getMaxRetryDelay() {
        return super.getMaxRetryDelay();
    }

    @Override
    public void setMaxRetryDelay(int maxRetryDelay) {
        super.setMaxRetryDelay(maxRetryDelay);
    }

    @Override
    public int getServerAckTimeout() {
        return super.getServerAckTimeout();
    }

    @Override
    public void setServerAckTimeout(int serverAckTimeout) {
        super.setServerAckTimeout(serverAckTimeout);
    }

    @Override
    public int getKeepAliveInterval() {
        return super.getKeepAliveInterval();
    }

    @Override
    public void setKeepAliveInterval(int keepAliveInterval) {
        super.setKeepAliveInterval(keepAliveInterval);
    }
}
