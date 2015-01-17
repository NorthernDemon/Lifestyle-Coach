package it.unitn.introsde.mbeans;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davie on 1/10/2015.
 */
@ManagedBean(name = "scheduleMBean", eager = true)
@SessionScoped
public class ScheduleMBean implements Serializable {
    String googleaccesstoken;

    public void submit() throws Exception {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
       googleaccesstoken = requestParameters.get("googleform:googleaccesstoken");
        System.out.println("googleaccesstoken>>>>>>  "+googleaccesstoken);
       //TODO
        //create calendar and and add events
    }
}
