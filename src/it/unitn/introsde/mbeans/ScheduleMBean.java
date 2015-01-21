package it.unitn.introsde.mbeans;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.wrapper.Schedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

/**
 * Created by davie on 1/10/2015.
 */
@ManagedBean(name = "scheduleMBean", eager = true)
@SessionScoped
public class ScheduleMBean implements Serializable {
    private static final Logger logger = LogManager.getLogger();

    private String successMessage;
    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private Map<String, Object> sessionMap = externalContext.getSessionMap();

    private String startDate;
    private String endDate;
    private String summary;
    private String location;

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

    public void InitialiseToken() throws Exception {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String googleaccesstoken = requestParameters.get("googleform:googleaccesstoken");
        sessionMap.put("googleaccesstoken", googleaccesstoken);
        logger.debug("googleaccesstoken>>> " + googleaccesstoken);
    }

    public void registerSchedule() throws Exception {
        Date startDate = getDate(getStartDate().split("-"));
        Date endDate = getDate(getEndDate().split("-"));

        Schedule schedule = new Schedule(startDate, endDate, getSummary(), getLocation());
        ResponseEntity<?> exchange = getResponse("schedule-process/?googleaccesstoken=" + sessionMap.get("googleaccesstoken"), schedule, HttpMethod.POST);
        logger.error("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.error("message payLoad === " + exchange);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            setSuccessMessage("oops! an error occured schedule not created");
        } else {
            setSuccessMessage("Schedule created Successfully!!");
        }
    }

    public List<Schedule> getEvents() {
        RestTemplate restTemplate = new RestTemplate();
        String url = ServiceConfiguration.getUrl() + "/event-process/?accessToken=" + sessionMap.get("googleaccesstoken");

        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(null), List.class);
        List<Schedule> schedules;
        if (exchange.getStatusCode().is2xxSuccessful()) {
            schedules = (List<Schedule>) exchange.getBody();
            logger.debug("Incoming [event-process] with Schedules=" + schedules + "");
            return schedules;
        } else {
            schedules = (List<Schedule>) exchange.getBody();
            logger.error("Incoming [event-process] with Schedules=" + schedules + "");
            return schedules;
        }
    }

    public ResponseEntity<?> getResponse(String restPath, Schedule schedule, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ServiceConfiguration.getUrl() + "/" + restPath;
        return restTemplate.exchange(url, httpMethod, createHeader(schedule), Schedule.class);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
