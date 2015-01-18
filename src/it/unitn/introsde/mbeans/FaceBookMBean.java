package it.unitn.introsde.mbeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by davie on 1/10/2015.
 */
@ManagedBean(name = "faceBookMBean", eager = true)
@SessionScoped
public class FaceBookMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();
    private String fbaccesstoken;

    public void submit() {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        fbaccesstoken = requestParameters.get("fbform:fbaccesstoken");

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("fbaccesstoken", fbaccesstoken);
    }
}
