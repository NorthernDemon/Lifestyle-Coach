package it.unitn.introsde.mbeans;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.MeasureType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by davie on 1/18/2015.
 */
@ManagedBean(name = "measureTypeMBean", eager = true)
@SessionScoped
public class MeasureTypeMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();
    private String successMessage;

    private String type;
    private String unit;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    private static HttpEntity<Object> createHeader(Object body) {
        MediaType applicationType = MediaType.APPLICATION_XML;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(applicationType));
        httpHeaders.setContentType(applicationType);
        return new HttpEntity<>(body, httpHeaders);
    }

    public void createMeasureType() {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.POST;
        String url = ServiceConfiguration.getUrl() + "/measuretype-process";
        MeasureType measureType = new MeasureType(getType(), getUnit());

        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(measureType), MeasureType.class);
        logger.error("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.error("message payLoad === " + exchange.getBody().toString());
        if (exchange.getBody() == null) {
            setSuccessMessage("oops! an error occured!!");
        } else {
            setSuccessMessage("Measure Type Registered Successfully!!");
        }
    }

    public List<MeasureType> getMeasureTypes() {
        ResponseEntity<?> exchange = getResponse("measureTypes-process", null, HttpMethod.GET);
        List<MeasureType> measureTypes = null;
        if (exchange.getStatusCode().is2xxSuccessful()) {
            measureTypes = (List<MeasureType>) exchange.getBody();
            logger.debug("Incoming [measureTypes-process] with MeasureType=" + measureTypes + "");
            return measureTypes;
        } else {
            measureTypes = (List<MeasureType>) exchange.getBody();
            logger.error("Incoming [measureTypes-process] with MeasureTypes=" + measureTypes + "");
            return measureTypes;
        }
    }

    public ResponseEntity<?> getResponse(String restPath, MeasureType measureType, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ServiceConfiguration.getUrl() + "/" + restPath;
        return restTemplate.exchange(url, httpMethod, createHeader(measureType), new ArrayList<MeasureType>().getClass());
    }

}
