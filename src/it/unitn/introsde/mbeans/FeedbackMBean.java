package it.unitn.introsde.mbeans;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.Motivation;
import it.unitn.introsde.wrapper.Workout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@ManagedBean(name = "feedbackMBean", eager = true)
@SessionScoped
public class FeedbackMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private Map<String, Object> sessionMap = externalContext.getSessionMap();
    private RestTemplate restTemplate = new RestTemplate();
    private HttpMethod httpMethod = HttpMethod.GET;

    private Motivation motivation;

    private Workout workout;

    public FeedbackMBean() {
        requestWorkout();
        requestMotivation();
    }

    private void requestMotivation() {
        String url = ServiceConfiguration.getUrl() + "/motivation-process/" + sessionMap.get("personId");
        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(null), Motivation.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            motivation = (Motivation) exchange.getBody();
            logger.debug("Motivation=" + motivation);
        } else {
            logger.error("Failed to fetch motivation");
        }
    }

    private void requestWorkout() {
        String url = ServiceConfiguration.getUrl() + "/workout-process/" + sessionMap.get("personId");
        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(null), Workout.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            workout = (Workout) exchange.getBody();
            logger.debug("Workout=" + workout);
        } else {
            logger.error("Failed to fetch workout");
        }
    }

    private static HttpEntity<Object> createHeader(Object body) {
        MediaType applicationType = MediaType.APPLICATION_XML;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(applicationType));
        httpHeaders.setContentType(applicationType);
        return new HttpEntity<>(body, httpHeaders);
    }

    public Motivation getMotivation() {
        return motivation;
    }

    public void setMotivation(Motivation motivation) {
        this.motivation = motivation;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
}
