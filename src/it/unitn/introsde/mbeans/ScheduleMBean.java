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
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by davie on 1/10/2015.
 */
@ManagedBean(name = "scheduleMBean", eager = true)
@SessionScoped
public class ScheduleMBean implements Serializable {
    private static final Logger logger = LogManager.getLogger();

    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private  Map<String, Object> sessionMap = externalContext.getSessionMap();

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

    public void InitialiseToken() throws Exception {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String googleaccesstoken = requestParameters.get("googleform:googleaccesstoken");
        sessionMap.put("googleaccesstoken", googleaccesstoken);
        logger.error("googleaccesstoken>>> " + googleaccesstoken);
    }

    public void registerSchedule() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.POST;
        String url = ServiceConfiguration.getUrl() + "/schedule-process";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = getDate(getStartDate().split("-"));
        Date endDate = getDate(getEndDate().split("-"));

        Schedule schedule = new Schedule(startDate, endDate, getSummary(), getLocation(), (String)sessionMap.get("googleaccesstoken"));

        ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(schedule), Schedule.class);
        logger.error("Status Code === " + exchange.getStatusCode().is2xxSuccessful());
        logger.error("message payLoad === " + exchange);
    }

//    public void createEvent(String accessToken) throws Exception {
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//        // Initialize Calendar service with valid OAuth credentials
//        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(httpTransport, jsonFactory, credential)
//                .setApplicationName("LifestyleCoach").build();
//
//        // Create and initialize a new event
//        Event event = new Event();
//        event.setSummary("walk 300km");
//        event.setLocation("queens park");
//
//        ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();
//        attendees.add(new EventAttendee().setEmail("matteo.matteovich@gmail.com"));
//// ...
//        event.setAttendees(attendees);
//
//        Date startDate = new Date();
//        Date endDate = new Date(startDate.getTime() + 3600000);
//        DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
//        event.setStart(new EventDateTime().setDateTime(start));
//        DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
//        event.setEnd(new EventDateTime().setDateTime(end));
//
//// Insert the new event
//        Event createdEvent = service.events().insert("primary", event).execute();
//
//        System.out.println(createdEvent.getId());
//    }
//
//    public void createCalendar(String accessToken) throws Exception {
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//
//        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(httpTransport, jsonFactory, credential)
//                .setApplicationName("LifestyleCoach").build();
//
//        // Create a new calendar
//        com.google.api.services.calendar.model.Calendar calendar = new Calendar();
//        calendar.setSummary("MateoCalendar");
//
//        // Insert the new calendar
//        com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();
//        System.out.println(createdCalendar.getId());
//    }
}
