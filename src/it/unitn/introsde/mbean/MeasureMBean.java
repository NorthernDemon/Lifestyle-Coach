package it.unitn.introsde.mbean;

import it.unitn.introsde.persistence.entity.Measure;
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

@ManagedBean(name = "measureMBean", eager = true)
@SessionScoped
public class MeasureMBean extends AbstractMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private String successMessage;

    private String measureType;
    private String value;

    public Measure registerMeasure() {
        ResponseEntity<?> personExchange = request("/getPersonById-process/" + sessionMap.get("personId"), HttpMethod.GET, Person.class, MediaType.APPLICATION_XML_VALUE);
        ResponseEntity<?> measureTypeExchange = request("/getMeasureTypeById-process/" + getMeasureType(), HttpMethod.GET, MeasureType.class, MediaType.APPLICATION_XML_VALUE);

        Measure measure = new Measure((Person) personExchange.getBody(), (MeasureType) measureTypeExchange.getBody(), Double.parseDouble(getValue()));
        ResponseEntity<?> exchange = request("/measure-process", HttpMethod.POST, Measure.class, measure, MediaType.APPLICATION_XML_VALUE);
        logger.debug("message payLoad === " + exchange.getBody().toString());
        if (exchange.getStatusCode().is2xxSuccessful()) {
            setSuccessMessage("Measure Registered Successfully!!");
        } else {
            setSuccessMessage("oops! an error occured!!");
        }
        return (Measure) exchange.getBody();
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
