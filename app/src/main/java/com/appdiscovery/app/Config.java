package com.appdiscovery.app;

public class Config {
    private static final Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }

    public String repoServerAddr = "http://10.222.160.194:888";
    public String centralServerAddr = "http://10.222.160.194:889";

    private Config() {
    }
}
