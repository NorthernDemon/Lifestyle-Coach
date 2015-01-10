package it.unitn.introsde.mbeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.annotation.PostConstruct;


import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by davie on 1/10/2015.
 */
@Named
@SessionScoped
public class FaceBookMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();
    public String submit() {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String fbaccesstoken = requestParameters.get("fbform:fbaccesstoken");
        logger.info("***** fbaccesstoken: " + fbaccesstoken);
        return fbaccesstoken;
    }
}
