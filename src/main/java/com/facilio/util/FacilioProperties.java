package com.facilio.util;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FacilioProperties {

    private static final String FACILIO_HOME = System.getProperty("user.home")+System.getProperty("file.separator")+"facilio"+System.getProperty("file.separator");
    private static final String CONFIG_FILE = FACILIO_HOME + "facilio.config";
    private static final Properties PROPERTIES = new Properties();

    private static final Logger LOGGER = LogManager.getLogger(FacilioProperties.class.getName());

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            LOGGER.info("Loading properties");
            File configFile = new File(CONFIG_FILE);
            if(configFile.exists()) {
                PROPERTIES.load(new FileReader(configFile));
                PROPERTIES.forEach((k,v) -> PROPERTIES.put(k, v.toString().trim()));
                checkConfig();
                LOGGER.info("Loaded properties successfully");
            } else {
                exit("facilio.config file is not present in " + FACILIO_HOME);
            }
        } catch (IOException e){
            exit("Exception while loading config " +  e.getMessage());
        }
    }

    private static void exit(String message) {
        System.err.println(message);
        System.exit(1);
    }

    private static boolean checkIfNullOrEmpty(String value) {
        return ( (value == null) || (value.isEmpty()) );
    }

    public static String getProperty(PropertyKeys key) {
        return PROPERTIES.getProperty(key.getKey());
    }

    private static void checkConfig() {
        LOGGER.info("Checking config");
        for(PropertyKeys key : PropertyKeys.values()) {
            if(checkIfNullOrEmpty(PROPERTIES.getProperty(key.getKey()))) {
                System.out.println(key.getKey() + " in " + CONFIG_FILE + " can't be null or empty");
            }
        }
        LOGGER.info("Checked config");
    }

    public static String getFacilioHome() {
        return FACILIO_HOME;
    }

    public static String getConfigFile() {
        return CONFIG_FILE;
    }
}
