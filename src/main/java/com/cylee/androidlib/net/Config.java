package com.cylee.androidlib.net;

public class Config {
    public static final String HTTP_URL_ONLINE = "http://www.baidu.com";
    private static String host = HTTP_URL_ONLINE;
    public static String getHost() {
        return host;
    }

    public static void setHost(String newHost) {
        host = newHost;
    }
}
