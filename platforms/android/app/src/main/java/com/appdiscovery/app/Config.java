package com.appdiscovery.app;

public class Config {
    private static final Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }

    public String serverAddr = "http://192.168.3.52:888";

    private Config() {
    }
}
