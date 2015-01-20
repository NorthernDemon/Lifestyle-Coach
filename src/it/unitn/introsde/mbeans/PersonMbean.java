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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by davie on 1/15/2015.
 */
@ManagedBean(name = "personMBean", eager = true)
@SessionScoped
public class PersonMbean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private Map<String, Object> sessionMap = externalContext.getSessionMap();

    private String successMessage;
    private String name;
    private String surname;
    private String birthday;

    private static Date getDate(String[] arr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(arr[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(arr[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
        return calendar.getTime();
    }

    private static HttpEntity<Object> createHeader(Object body) {
        MediaType applicationType = MediaType.APPLICATION_XML;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(applicationType));
        httpHeaders.setContentType(applicationType);
        return new HttpEntity<>(body, httpHeaders);
    }

    public PersonMbean() {

        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.GET;
        String url = ServiceConfiguration.getUrl() + "/fbuser-process/" + sessionMap.get("fbaccesstoken");

        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(null), Person.class);
        logger.error("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.error("message payLoad === " + exchange);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Person person = (Person) exchange.getBody();
            setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(person.getBirthday()));
            setName(person.getName());
            setSurname(person.getSurname());
        } else {
            logger.debug("request not successful");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public void registerPerson() {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.POST;
        String url = ServiceConfiguration.getUrl() + "/person-process";

        Date birthDate = getDate(getBirthday().split("-"));
        Person person = new Person(getName(), getSurname(), birthDate, sessionMap.get("fbaccesstoken").toString(), sessionMap.get("googleaccesstoken").toString());

        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(person), Person.class);
        logger.error("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.error("message payLoad === " + exchange.getBody().toString());
        if (exchange.getBody() == null) {
            setSuccessMessage("oops! an error occured!!");
        } else {
            setSuccessMessage("Person Registered Successfully!!");
        }
    }
}
