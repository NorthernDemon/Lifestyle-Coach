package it.unitn.introsde.persistence.datasource;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import com.restfb.types.User;
import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class FaceBookDatasource {

    private static final Logger logger = LogManager.getLogger();

    public Person getUser(String accessToken) throws ParseException {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_2);
        User user = facebookClient.fetchObject("me", User.class);
        logger.info("name>> " + user.getFirstName());
        logger.info("accessToken>> " + accessToken);
        logger.info("me>> " + user);
        logger.info("me.getBirthday>> " + user.getBirthday());
        return new Person(user.getFirstName(), user.getLastName(), new SimpleDateFormat("MM/dd/yyyy").parse(user.getBirthday()), accessToken);
    }

    public <T>void postToWall(String accessToken,T object) {
        Goal goal = (Goal)object;
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_2);
        FacebookType publishMessageResponse = facebookClient.publish("me/feed", FacebookType.class, Parameter.with("message","Goal to Achieve: " +
                goal.getMessage()+" from: "+goal.getStart()+" to "+goal.getEnd()+" Created by: "+goal.getPerson().getName() ));
        logger.debug("Published message ID: " + publishMessageResponse.getId());
    }
}
