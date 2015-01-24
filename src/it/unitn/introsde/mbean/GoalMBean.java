package it.unitn.introsde.mbean;

import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.MeasureType;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "goalMBean", eager = true)
@SessionScoped
public class GoalMBean extends AbstractMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private String successMessage;

    private String startDate;
    private String endDate;
    private String message;
    private String Creator;
    private String person;
    private String measureType;

    @SuppressWarnings("unchecked")
    public List<Person> getPeople() {
        ResponseEntity<?> exchange = request("people-process", HttpMethod.GET, List.class, MediaType.APPLICATION_XML_VALUE);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            List<Person> people = (List<Person>) exchange.getBody();
            logger.debug("Incoming [people-process] with People=" + people);
            return people;
        } else {
            List<Person> people = (List<Person>) exchange.getBody();
            logger.error("Incoming [people-process] with People=" + people);
            return people;
        }
    }

    public void registerGoal() {
        Date startDate = getDate(getEndDate().split("-"));
        Date endDate = getDate(getStartDate().split("-"));

        ResponseEntity<?> creatorExchange = request("/getPersonById-process/" + getCreator(), HttpMethod.GET, Person.class, MediaType.APPLICATION_XML_VALUE);
        ResponseEntity<?> measureTypeExchange = request("/getMeasureTypeById-process/" + getMeasureType(), HttpMethod.GET, MeasureType.class, MediaType.APPLICATION_XML_VALUE);

        Goal goal = new Goal((Person) creatorExchange.getBody(), (Person) creatorExchange.getBody(), (MeasureType) measureTypeExchange.getBody(), 0, getMessage(), startDate, endDate);
        ResponseEntity<?> exchange = request("/goal-process?fbAccessToken=" + sessionMap.get("fbaccesstoken"), HttpMethod.POST, Goal.class, goal, MediaType.APPLICATION_XML_VALUE);
        logger.debug("message payLoad === " + exchange.getBody().toString());
        if (exchange.getStatusCode().is2xxSuccessful()) {
            setSuccessMessage("Goal Registered Successfully!!");
        } else {
            setSuccessMessage("oops! an error occured!!");
        }
    }

    @SuppressWarnings("unchecked")
    public List<Goal> getGoals() {
        ResponseEntity<?> exchange = request("/goals-process", HttpMethod.GET, List.class, MediaType.APPLICATION_XML_VALUE);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            List<Goal> goals = (List<Goal>) exchange.getBody();
            logger.debug("Incoming [goals-process] with goals=" + goals);
            return goals;
        } else {
            List<Goal> goals = (List<Goal>) exchange.getBody();
            logger.error("Incoming [goals-process] with goals=" + goals);
            return goals;
        }
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }
}
