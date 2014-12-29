package it.unitn.introsde;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads property file with service configuration
 */
public abstract class ServiceConfiguration {

    public final static String SCHEMA = "lifestylecoach";

    public final static String NAME = "/" + SCHEMA;

    private static int port;

    private static String host;

    private static String url;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("system.properties"));

            port = Integer.parseInt(properties.getProperty("port"));
            host = properties.getProperty("host");
            url = "http://" + host + ":" + port + NAME;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getPort() {
        return port;
    }

    public static String getHost() {
        return host;
    }

    public static String getName() {
        return NAME;
    }

    public static String getUrl() {
        return url;
    }
}
