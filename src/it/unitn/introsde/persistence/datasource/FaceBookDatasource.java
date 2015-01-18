package it.unitn.introsde.persistence.datasource;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Page;
import com.restfb.types.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class FaceBookDatasource {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String args[]) {
        //getting details mateo has permitted us to access
        FacebookClient facebookClient = new DefaultFacebookClient("fbaccesstoken");
        User user = facebookClient.fetchObject("me", User.class);
        Page page = facebookClient.fetchObject("cocacola", Page.class);

        System.out.println("User name: " + user.getName());
        System.out.println("Page likes: " + page.getLikes());

        //posting on mateos timeline
        FacebookType publishMessageResponse = facebookClient.publish("me/feed", FacebookType.class, Parameter.with("message", "RestFB test"));
        logger.error("Published message ID: " + publishMessageResponse.getId());
    }
}
