package it.unitn.introsde.mbean;

import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean(name = "faceBookMBean", eager = true)
@SessionScoped
public class FaceBookMBean extends AbstractMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private String fbaccesstoken;

    public void submit() {
        fbaccesstoken = externalContext.getRequestParameterMap().get("fbform:fbaccesstoken");
        logger.info("mbeanfbaccesstoken " + fbaccesstoken);
        sessionMap.put("fbaccesstoken", fbaccesstoken);
        registerPerson(getFbUser());
    }

    private Person getFbUser() {
        ResponseEntity<?> exchange = request("/fbUser-process/" + sessionMap.get("fbaccesstoken"), HttpMethod.GET, Person.class, MediaType.APPLICATION_XML_VALUE);
        logger.debug("message payLoad === " + exchange);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            return (Person) exchange.getBody();
        } else {
            logger.error("request not successful");
            return null;
        }
    }

    private Person registerPerson(Person person) {
        ResponseEntity<?> exchange = request("/person-process", HttpMethod.POST, Person.class, person, MediaType.APPLICATION_XML_VALUE);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            person = (Person) exchange.getBody();
            logger.debug("created Person=== " + person);
            sessionMap.put("personId", person.getId());
            return person;
        } else {
            logger.error("failed to create person!");
            return null;
        }
    }
}
