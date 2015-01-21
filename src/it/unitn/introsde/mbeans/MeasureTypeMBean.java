package it.unitn.introsde.mbeans;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.MeasureType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "measureTypeMBean", eager = true)
@SessionScoped
public class MeasureTypeMBean extends AbstractMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private String successMessage;

    private String type;
    private String unit;

    public void createMeasureType() {
        MeasureType measureType = new MeasureType(getType(), getUnit());
        ResponseEntity<?> exchange = request("/measureType-process", HttpMethod.POST, MeasureType.class, measureType, MediaType.APPLICATION_XML_VALUE);
        logger.debug("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.debug("message payLoad === " + exchange.getBody().toString());
        if (exchange.getBody() == null) {
            setSuccessMessage("oops! an error occured!!");
        } else {
            setSuccessMessage("Measure Type Registered Successfully!!");
        }
    }

    @SuppressWarnings("unchecked")
    public List<MeasureType> getMeasureTypes() {
        ResponseEntity<?> exchange = request("measureTypes-process", HttpMethod.GET, List.class, MediaType.APPLICATION_XML_VALUE);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            List<MeasureType> measureTypes = (List<MeasureType>) exchange.getBody();
            logger.debug("Incoming [measureTypes-process] with MeasureType=" + measureTypes + "");
            return measureTypes;
        } else {
            List<MeasureType> measureTypes = (List<MeasureType>) exchange.getBody();
            logger.error("Incoming [measureTypes-process] with MeasureTypes=" + measureTypes + "");
            return measureTypes;
        }
    }

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
}
