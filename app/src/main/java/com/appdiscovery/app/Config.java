package com.appdiscovery.app;

public class Config {
    private static final Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }

    public String repoServerAddr = "http://ad-app-repo-dynamic.kevinwang.cc:888";
    public String canonicalRepoServerAddr = "http://ad-central-server.kevinwang.cc:888";
    public String centralServerAddr = "http://ad-central-server.kevinwang.cc:889";

    public String lanServerAddr = "http://lan-app-repo-server.appd";

    private Config() {
    }
}
