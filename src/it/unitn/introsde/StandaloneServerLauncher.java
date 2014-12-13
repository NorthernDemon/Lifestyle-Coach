package it.unitn.introsde;

import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class StandaloneServerLauncher {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname(StandaloneClientLauncher.IP);
        tomcat.setPort(StandaloneClientLauncher.PORT);
        tomcat.addWebapp("/", new File("webapp/").getAbsolutePath());
        tomcat.start();
        System.out.println("Standalone server is up at: " + tomcat.getHost().getName() + ':' + tomcat.getConnector().getPort() + StandaloneClientLauncher.SERVER_STUDENT_NAME);
        printPossibleIP();
        tomcat.getServer().await();
    }

    /**
     * List other possible ip addresses for clients to connect
     *
     * @throws SocketException
     */
    private static void printPossibleIP() throws SocketException {
        System.out.println("Other possible IPs:");
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        for (; networkInterfaces.hasMoreElements(); ) {
            Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
            for (; inetAddresses.hasMoreElements(); ) {
                String hostAddress = inetAddresses.nextElement().getHostAddress();
                if (hostAddress.contains(".") && !"127.0.0.1".equals(hostAddress) && !StandaloneClientLauncher.IP.equals(hostAddress)) {
                    System.out.println('-' + hostAddress);
                }
            }
        }
    }
}
