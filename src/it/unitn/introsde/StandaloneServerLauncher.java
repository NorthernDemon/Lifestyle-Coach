package it.unitn.introsde;

import org.apache.catalina.LifecycleException;
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
        final Tomcat tomcat = new Tomcat();
        tomcat.setHostname(ServiceConfiguration.getHost());
        tomcat.setPort(ServiceConfiguration.getPort());
        tomcat.addWebapp("", new File("webapp/").getAbsolutePath());
        tomcat.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("Stopping the Tomcat server... ");
                try {
                    if (tomcat.getServer().getState().isAvailable()) {
                        tomcat.getServer().stop();
                    }
                } catch (LifecycleException e) {
                    logger.error(e);
                }
            }
        });
        logger.info("WSDL for SOAP is published at: " + ServiceConfiguration.getWsdl());
        logger.info("REST service is up at: " + tomcat.getHost().getName() + ':' + tomcat.getConnector().getPort() + ServiceConfiguration.getName());
        logger.info("Standalone server is up at: " + tomcat.getHost().getName() + ':' + tomcat.getConnector().getPort());
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
        while (networkInterfaces.hasMoreElements()) {
            Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                String hostAddress = inetAddresses.nextElement().getHostAddress();
                if (hostAddress.contains(".") && !"127.0.0.1".equals(hostAddress) && !ServiceConfiguration.getHost().equals(hostAddress)) {
                    logger.info(hostAddress);
                }
            }
        }
    }
}
