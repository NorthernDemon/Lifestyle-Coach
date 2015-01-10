package it.unitn.introsde;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class StandaloneServerLauncher extends Tomcat {

    private static final Logger logger = LogManager.getLogger();
    String pathToGlobalWebXml = new File("webapp/WEB-INF/.web.xml").getAbsolutePath();

    public static void main(String[] args) throws Exception {
        final Tomcat tomcat = new Tomcat();
        String webappFilePath = new File("webapp/").getAbsolutePath();

        tomcat.setHostname(ServiceConfiguration.getHost());
        tomcat.setPort(ServiceConfiguration.getPort());
        tomcat.addWebapp("/",webappFilePath);
        //tomcat.addWebapp("/", new File("webapp/").getAbsolutePath());
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

    @Override
    public Context addWebapp(String contextPath, String webappFilePath)
            throws ServletException {

        Context context = null;
        try {
            context = new StandardContext();
            context.setName(contextPath);
            context.setPath(contextPath);
            context.setDocBase(webappFilePath);
            context.setRealm(this.getHost().getRealm());
            ContextConfig contextConfig = new ContextConfig();
            context.addLifecycleListener(contextConfig);
            if (new File(pathToGlobalWebXml).exists()) {
                contextConfig.setDefaultWebXml(pathToGlobalWebXml);
            } else {
                contextConfig.setDefaultWebXml("org/apache/catalin/startup/NO_DEFAULT_XML");
            }
            host.addChild(context);
        } catch (Exception e) {
           System.out.println("Error deploying webapp"+e);
        }
        return context;
    }
}
