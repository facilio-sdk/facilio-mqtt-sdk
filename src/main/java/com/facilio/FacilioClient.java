package com.facilio;

import com.facilio.aws.mqtt.FacilioMQTTClient;
import com.facilio.util.FacilioProperties;
import com.facilio.util.PropertyKeys;
import org.json.simple.JSONObject;

public class FacilioClient {

    public static void main(String[] args) {

        FacilioMQTTClient client = FacilioMQTTClient.getMQTTClient();
        while (true) {
            if (client != null) {
                client.publish(FacilioProperties.getProperty(PropertyKeys.TOPIC), getData());
            } else {
                System.out.println("Client is not initialized properly, check whether the properties are configured properly in " + FacilioProperties.getConfigFile());
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getData() {
        JSONObject object = new JSONObject();
        object.put("data", "test-" + System.currentTimeMillis());
        
        // your code goes here

        return object.toJSONString();
    }
}
