package it.unitn.introsde.mbeans;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.process.AbstractProcess;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Convenient class to abstract managed beans for message passing
 */
public abstract class AbstractMBean extends AbstractProcess {

    private RestTemplate restTemplate = new RestTemplate();

    protected ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    protected Map<String, Object> sessionMap = externalContext.getSessionMap();

    protected <T> ResponseEntity<?> request(String restPath, HttpMethod httpMethod, Class<T> clazz, String mediaType) {
        return request(restPath, httpMethod, clazz, null, mediaType);
    }

    protected <T> ResponseEntity<?> request(String restPath, HttpMethod httpMethod, Class<T> clazz, T object, String mediaType) {
        return restTemplate.exchange(ServiceConfiguration.getUrl() + "/" + restPath, httpMethod, createHeader(mediaType, object), clazz);
    }

    protected static Date getDate(String[] arr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(arr[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(arr[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
        return calendar.getTime();
    }
}
