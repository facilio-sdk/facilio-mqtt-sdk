package com.facilio.util;

public enum PropertyKeys {
    END_POINT("endpoint"),
    KEY_FILE("privateKeyFile"),
    CERT_FILE("certificateFile"),
    TOPIC("topic"),
    CLIENT_ID("clientId");

    private String key;

    PropertyKeys(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }

}
