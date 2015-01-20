package it.unitn.introsde.mbeans;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.MeasureType;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.Schedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by davie on 1/10/2015.
 */
@ManagedBean(name = "goalMBean", eager = true)
@SessionScoped
public class GoalMBean implements Serializable {
    private static final Logger logger = LogManager.getLogger();
    private String startDate;
    private String endDate;
    private String message;
    private String successMessage;
    private String Creator;
    private String person;
    private String measureType;

    private static HttpEntity<Object> createHeader(Object body) {
        MediaType applicationType = MediaType.APPLICATION_XML;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(applicationType));
        httpHeaders.setContentType(applicationType);
        return new HttpEntity<>(body, httpHeaders);
    }

    private static Date getDate(String[] arr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(arr[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(arr[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
        return calendar.getTime();
    }


    public List<Person> getPeople() {
        ResponseEntity<?> exchange = getResponse("people-process", null, HttpMethod.GET);
        List<Person> people = null;
        if (exchange.getStatusCode().is2xxSuccessful()) {
            people = (List<Person>) exchange.getBody();
            logger.debug("Incoming [people-process] with People=" + people + "");
            return people;
        } else {
            people = (List<Person>) exchange.getBody();
            logger.error("Incoming [people-process] with People=" + people + "");
            return people;
        }
    }

    public void registerGoal() {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.POST;
        String url = ServiceConfiguration.getUrl();
        Date startDate = getDate(getEndDate().split("-"));
        Date endDate = getDate(getStartDate().split("-"));

        ResponseEntity<?> personExchange = getGenericResponse("/getpersonbyid-process/" + getPerson(), HttpMethod.GET, Person.class, null);
        ResponseEntity<?> creatorExchange = getGenericResponse("/getpersonbyid-process/" + getCreator(), HttpMethod.GET, Person.class, null);
        ResponseEntity<?> measureTypeExchange = getGenericResponse("/getmeasureTypeById-process/" + getMeasureType(), HttpMethod.GET, MeasureType.class, null);

        Goal goal = new Goal((Person) creatorExchange.getBody(), (Person) creatorExchange.getBody(), (MeasureType) measureTypeExchange.getBody(), 0, getMessage(), startDate, endDate);
        ResponseEntity<?> exchange = restTemplate.exchange(url + "/goal-process", httpMethod, createHeader(goal), Goal.class);
        logger.debug("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.debug("message payLoad === " + exchange.getBody().toString());
        if (exchange.getStatusCode().is2xxSuccessful()) {
            setSuccessMessage("Goal Registered Successfully!!");
        } else {
            setSuccessMessage("oops! an error occured!!");
        }
    }

    public ResponseEntity<?> getResponse(String restPath, Schedule schedule, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ServiceConfiguration.getUrl() + "/" + restPath;
        return restTemplate.exchange(url, httpMethod, createHeader(schedule), List.class);
    }

    public <T> ResponseEntity<?> getGenericResponse(String restPath, HttpMethod httpMethod, Class<T> clazz, T object) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ServiceConfiguration.getUrl() + "/" + restPath;
        return restTemplate.exchange(url, httpMethod, createHeader(object), clazz);
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
