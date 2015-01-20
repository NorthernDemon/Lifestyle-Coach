package it.unitn.introsde.mbeans;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.MeasureType;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by davie on 1/10/2015.
 */
@ManagedBean(name = "measureMBean", eager = true)
@SessionScoped
public class MeasureMBean implements Serializable {
    private static final Logger logger = LogManager.getLogger();
    private String measureType;
    private String value;

    private String successMessage;

    public Measure registerMeasure(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();

        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.POST;
        String url = ServiceConfiguration.getUrl()+"/measure-process";
        ResponseEntity<?> personExchange = getGenericResponse("/getpersonbyid-process/" + sessionMap.get("personId"), HttpMethod.GET, Person.class, null);
        ResponseEntity<?> measureTypeExchange = getGenericResponse("/getmeasureTypeById-process/" + getMeasureType(), HttpMethod.GET, MeasureType.class, null);

        Measure measure = new Measure((Person)personExchange.getBody(),(MeasureType)measureTypeExchange.getBody(),Double.parseDouble(getValue()));
        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(measure), Measure.class);
        logger.debug("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.debug("message payLoad === " + exchange.getBody().toString());
        if (exchange.getStatusCode().is2xxSuccessful()) {
            setSuccessMessage("Measure Registered Successfully!!");
        } else {
            setSuccessMessage("oops! an error occured!!");
        }
        return (Measure)exchange.getBody();
    }

    public <T> ResponseEntity<?> getGenericResponse(String restPath, HttpMethod httpMethod, Class<T> clazz, T object) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ServiceConfiguration.getUrl() + "/" + restPath;
        return restTemplate.exchange(url, httpMethod, createHeader(object), clazz);
    }

    private static HttpEntity<Object> createHeader(Object body) {
        MediaType applicationType = MediaType.APPLICATION_XML;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(applicationType));
        httpHeaders.setContentType(applicationType);
        return new HttpEntity<>(body, httpHeaders);
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
