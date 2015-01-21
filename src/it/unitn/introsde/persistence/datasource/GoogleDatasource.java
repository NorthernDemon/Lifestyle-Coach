package it.unitn.introsde.persistence.datasource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import it.unitn.introsde.wrapper.Schedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class GoogleDatasource {
    private static final Logger logger = LogManager.getLogger();

    public void createEvent(Schedule schedule) throws Exception {
        logger.debug("access-token>> " + schedule.getGoogleAccessToken());
        logger.debug("schedule object>>> " + schedule.toString());

        // Create and initialize a new event
        Event event = new Event();
        event.setSummary(schedule.getSummary());
        event.setLocation(schedule.getLocation());

        DateTime start = new DateTime(schedule.getStartDate(), TimeZone.getTimeZone("UTC"));
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(schedule.getEndDate(), TimeZone.getTimeZone("UTC"));
        event.setEnd(new EventDateTime().setDateTime(end));

        // Insert the new event
        Event createdEvent = getGoogleService(schedule.getGoogleAccessToken()).events().insert("primary", event).execute();
        logger.info("createdEvent>>> " + createdEvent);
    }

    public List<Schedule> getCalendarEvent(String accessToken) throws Exception {
        Events events = getGoogleService(accessToken).events().list("primary").execute();
        logger.debug("events>>>> " + events);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Schedule> schedules = new ArrayList<>(events.size());
        for (Event event : events.getItems()) {
            //logger.debug("event date"+event.);
            schedules.add(new Schedule(dateFormat.parse(dateFormat.format(new Date())),dateFormat.parse(dateFormat.format(new Date())), event.getSummary(), event.getLocation(), accessToken));
        }
        return schedules;
    }

    public Calendar getGoogleService(String accessToken) throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        return new Calendar.Builder(httpTransport, jsonFactory, credential).setApplicationName("LifestyleCoach").build();
    }
}
