package com.facilio.aws.mqtt;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class Message extends AWSIotMessage {


    private static final Logger LOGGER = LogManager.getLogger(Message.class.getName());

    private String filename = null;

    public Message(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    public Message(String topic, AWSIotQos qos, byte[] payload) {
        super(topic, qos, payload);
    }

    public Message(String topic, AWSIotQos qos, String payload) {
        super(topic, qos, payload);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
    }

    @Override
    public void onFailure() {
        handleFailure();
    }

    @Override
    public void onTimeout() {
        handleFailure();
    }

    private void handleFailure() {
        System.out.println("failed to send message");
    }
}
