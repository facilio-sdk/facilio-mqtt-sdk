package com.facilio.aws.mqtt;

import com.amazonaws.services.iot.client.*;
import com.amazonaws.services.iot.client.core.AwsIotRuntimeException;
import com.facilio.util.FacilioProperties;
import com.facilio.util.KeyStorePasswordPair;
import com.facilio.util.PropertyKeys;

import java.security.KeyStore;
/**
<br>This class is the main interface of the Facilio MQTT library. It provides both blocking and non-blocking methods for interacting with Facilio services over the MQTT protocol. With this client, one can directly publish messages to the Facilio service and subscribe or unsubscribe to any pub/sub topics.<br> 
<br>There are two types of connections this SDK supports to connect to the AWS IoT service:<br><br>

<b>MQTT</b> (over TLS 1.2) with X.509 certificate based mutual authentication<br>
<b>MQTT</b>  over Secure WebSocket with AWS SigV4 authentication<br>
For <b>MQTT</b>  over TLS, a KeyStore containing a valid device certificate and private key is required for instantiating the client. Password for decrypting the private key in the KeyStore must also be provided.<br>


<br>For more information about Facilio IoT service, please refer to the Facilio developer guide.<br><br>

To use the client directly, a typical flow would be like the below, and since methods in this class are thread-safe, publish and subscribe can be called from different threads.<br><br>


   FacilioMQTTClient client = new FacilioMQTTClient(...);<br><br>
   
   client.connect();<br><br><br>
   
   ...<br>
   client.subscribe(topic, ...)<br><br>
   ...<br>
   client.publish(message, ...)<br><br>


  
* 
* 
* @author Yoge
*
*/

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
    /**
     * @param topic unique string associated with facilio account
     */
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
