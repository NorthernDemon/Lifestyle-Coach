package it.unitn.introsde;

import it.unitn.introsde.util.NetworkUtil;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

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
                    logger.error("Failed to stop Tomcat server", e);
                }
            }
        });
        logger.info("WSDL for SOAP is published at: " + ServiceConfiguration.getWsdl());
        logger.info("REST service is up at: " + tomcat.getHost().getName() + ':' + tomcat.getConnector().getPort() + ServiceConfiguration.getName());
        logger.info("Standalone server is up at: " + tomcat.getHost().getName() + ':' + tomcat.getConnector().getPort());
        NetworkUtil.printMachineIPv4();
        tomcat.getServer().await();
    }
}
