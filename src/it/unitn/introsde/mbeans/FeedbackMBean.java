package it.unitn.introsde.mbeans;

import it.unitn.introsde.wrapper.Awareness;
import it.unitn.introsde.wrapper.Motivation;
import it.unitn.introsde.wrapper.Workout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean(name = "feedbackMBean", eager = true)
@SessionScoped
public class FeedbackMBean extends AbstractMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private Motivation motivation;

    private Workout workout;

    private Awareness awareness;

    public FeedbackMBean() {
        requestWorkout();
        requestMotivation();
        requestAwareness();
    }

    private void requestMotivation() {
        ResponseEntity<?> exchange = request("/motivation-process/" + sessionMap.get("personId"), HttpMethod.GET, Motivation.class, MediaType.APPLICATION_XML_VALUE);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            motivation = (Motivation) exchange.getBody();
            logger.debug("Motivation=" + motivation);
        } else {
            logger.error("Failed to fetch motivation");
        }
    }

    private void requestWorkout() {
        ResponseEntity<?> exchange = request("/workout-process/" + sessionMap.get("personId"), HttpMethod.GET, Workout.class, MediaType.APPLICATION_XML_VALUE);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            workout = (Workout) exchange.getBody();
            logger.debug("Workout=" + workout);
        } else {
            logger.error("Failed to fetch workout");
        }
    }

    private void requestAwareness() {
        ResponseEntity<?> exchange = request("/awareness-process/" + sessionMap.get("personId"), HttpMethod.GET, Awareness.class, MediaType.APPLICATION_XML_VALUE);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            awareness = (Awareness) exchange.getBody();
            logger.debug("Awareness=" + awareness);
        } else {
            logger.error("Failed to fetch awareness");
        }
    }

    public Awareness getAwareness() {
        return awareness;
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
