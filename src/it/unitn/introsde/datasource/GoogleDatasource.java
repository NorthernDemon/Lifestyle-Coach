package it.unitn.introsde.datasource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import it.unitn.introsde.wrapper.Schedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

@Component
public class GoogleDatasource {

    private static final Logger logger = LogManager.getLogger();

    public Event createEvent(Schedule schedule, String googleAccessToken) throws Exception {
        Event createdEvent = getGoogleService(googleAccessToken).events().insert("primary", getEvent(schedule)).execute();
        logger.info("createdEvent>>> " + createdEvent);
        return createdEvent;
    }

    private Event getEvent(Schedule schedule) {
        Event event = new Event();
        event.setSummary(schedule.getSummary());
        event.setLocation(schedule.getLocation());
        event.setStart(getDate(schedule.getStartDate()));
        event.setEnd(getDate(schedule.getEndDate()));
        return event;
    }

    private EventDateTime getDate(Date date) {
        return new EventDateTime().setDateTime(new DateTime(date, TimeZone.getTimeZone("UTC")));
    }

    public List<Schedule> getCalendarEvent(String accessToken) throws Exception {
        List<Schedule> schedules = new LinkedList<>();
        for (Event event : getGoogleService(accessToken).events().list("primary").execute().getItems()) {
            schedules.add(new Schedule(new Date(), new Date(), event.getSummary(), event.getLocation()));
        }
        return schedules;
    }

    private Calendar getGoogleService(String accessToken) throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        return new Calendar.Builder(httpTransport, jsonFactory, credential).setApplicationName("LifestyleCoach").build();
    }
}
