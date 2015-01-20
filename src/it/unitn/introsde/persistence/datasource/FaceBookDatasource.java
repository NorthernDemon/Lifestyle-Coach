package it.unitn.introsde.persistence.datasource;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.User;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FaceBookDatasource {

    private static final Logger logger = LogManager.getLogger();

    public Person getUser(String accessToken) throws ParseException {
        //getting details mateo has permitted us to access
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
        User me = facebookClient.fetchObject("me", User.class);
        logger.info("name>> "+me.getFirstName());
        logger.info("accessToken>> "+accessToken);
        logger.info("me>> "+me);
        logger.info("me.getBirthday>> "+me.getBirthday());
        return new Person(me.getFirstName(),me.getLastName(),new SimpleDateFormat("MM/dd/yyyy").parse(me.getBirthday()) );
    }

    public void posttoFacebook(String accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
        FacebookType publishMessageResponse = facebookClient.publish("me/feed", FacebookType.class, Parameter.with("message", "RestFB test"));
        logger.debug("Published message ID: " + publishMessageResponse.getId());
    }
}
