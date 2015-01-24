package it.unitn.introsde.mbean;

import it.unitn.introsde.wrapper.Schedule;
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

@ManagedBean(name = "scheduleMBean", eager = true)
@SessionScoped
public class ScheduleMBean extends AbstractMBean implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private String successMessage;

    private String startDate;
    private String endDate;
    private String summary;
    private String location;

    public void InitialiseToken() throws Exception {
        String googleAccessToken = externalContext.getRequestParameterMap().get("googleform:googleaccesstoken");
        sessionMap.put("googleaccesstoken", googleAccessToken);
        logger.debug("googleaccesstoken>>> " + googleAccessToken);
    }

    public void registerSchedule() throws Exception {
        Date startDate = getDate(getStartDate().split("-"));
        Date endDate = getDate(getEndDate().split("-"));

        Schedule schedule = new Schedule(startDate, endDate, getSummary(), getLocation());
        ResponseEntity<?> exchange = request("schedule-process/?googleAccessToken=" + sessionMap.get("googleaccesstoken"), HttpMethod.POST, Schedule.class, schedule, MediaType.APPLICATION_XML_VALUE);
        logger.debug("message payLoad === " + exchange);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            setSuccessMessage("Schedule created Successfully!!");
        } else {
            setSuccessMessage("oops! an error occured schedule not created");
        }
    }

    @SuppressWarnings("unchecked")
    public List<Schedule> getEvents() {
        ResponseEntity<?> exchange = request("/event-process/?accessToken=" + sessionMap.get("googleaccesstoken"), HttpMethod.GET, List.class, MediaType.APPLICATION_XML_VALUE);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            List<Schedule> schedules = (List<Schedule>) exchange.getBody();
            logger.debug("Incoming [event-process] with Schedules=" + schedules);
            return schedules;
        } else {
            List<Schedule> schedules = (List<Schedule>) exchange.getBody();
            logger.error("Incoming [event-process] with Schedules=" + schedules);
            return schedules;
        }
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
