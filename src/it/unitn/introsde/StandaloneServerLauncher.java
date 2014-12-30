package it.unitn.introsde;

import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class StandaloneServerLauncher {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname(ServiceConfiguration.getHost());
        tomcat.setPort(ServiceConfiguration.getPort());
        tomcat.addWebapp("/", new File("webapp/").getAbsolutePath());
        tomcat.start();
        logger.info("Standalone server is up at: " + tomcat.getHost().getName() + ':' + tomcat.getConnector().getPort() + ServiceConfiguration.getName());
        printPossibleIP();
        tomcat.getServer().await();
    }

    /**
     * List other possible ip addresses for clients to connect
     *
     * @throws SocketException
     */
    private static void printPossibleIP() throws SocketException {
        logger.info("Other possible IPs:");
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        for (; networkInterfaces.hasMoreElements(); ) {
            Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
            for (; inetAddresses.hasMoreElements(); ) {
                String hostAddress = inetAddresses.nextElement().getHostAddress();
                if (hostAddress.contains(".") && !"127.0.0.1".equals(hostAddress) && !ServiceConfiguration.getHost().equals(hostAddress)) {
                    logger.info(hostAddress);
                }
            }
        }
    }
}
