package it.unitn.introsde.mbeans;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by davie on 1/10/2015.
 */
@ManagedBean(name = "faceBookMBean", eager = true)
@SessionScoped
public class FaceBookMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private Map<String, Object> sessionMap = externalContext.getSessionMap();

    private String fbaccesstoken;

    private static HttpEntity<Object> createHeader(Object body) {
        MediaType applicationType = MediaType.APPLICATION_XML;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(applicationType));
        httpHeaders.setContentType(applicationType);
        return new HttpEntity<>(body, httpHeaders);
    }

    public void submit() {
        fbaccesstoken = externalContext.getRequestParameterMap().get("fbform:fbaccesstoken");
        logger.info("mbeanfbaccesstoken " + fbaccesstoken);
        sessionMap.put("fbaccesstoken", fbaccesstoken);

        Person person = getFbUser();
        faceBookProcessCentricSavePerson(person);
    }

    private Person getFbUser() {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.GET;
        String url = ServiceConfiguration.getUrl() + "/fbuser-process/" + sessionMap.get("fbaccesstoken");

        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(null), Person.class);
        logger.error("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.error("message payLoad === " + exchange);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            return (Person) exchange.getBody();
        } else {
            logger.debug("request not successful");
            return null;
        }
    }

    public Person faceBookProcessCentricSavePerson(Person person) {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.POST;
        String url = ServiceConfiguration.getUrl() + "/person-process";

        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(person), Person.class);
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
