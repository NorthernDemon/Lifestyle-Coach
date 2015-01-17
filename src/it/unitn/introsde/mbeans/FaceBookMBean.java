package it.unitn.introsde.mbeans;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Page;
import com.restfb.types.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
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
    String fbaccesstoken = null;

    public void submit() {
        System.out.println("reaching here>>>");
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        fbaccesstoken = requestParameters.get("fbform:fbaccesstoken");

        //getting details mateo has permitted us to access
        FacebookClient facebookClient = new DefaultFacebookClient(fbaccesstoken);
        User user = facebookClient.fetchObject("me", User.class);
        Page page = facebookClient.fetchObject("cocacola", Page.class);

        System.out.println("User name: " + user.getName());
        System.out.println("Page likes: " + page.getLikes());

        //posting on mateos timeline
        FacebookType publishMessageResponse = facebookClient.publish("me/feed", FacebookType.class, Parameter.with("message", "RestFB test"));
        System.out.println("Published message ID: " + publishMessageResponse.getId());
    }
}
