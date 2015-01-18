package it.unitn.introsde.persistence.datasource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import it.unitn.introsde.wrapper.Schedule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

@Component
public class GoogleDatasource {
    public void createEvent(Schedule schedule)throws Exception{
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = new GoogleCredential().setAccessToken(schedule.getGoogleAccessToken());
        // Initialize Calendar service with valid OAuth credentials
        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("LifestyleCoach").build();

        // Create and initialize a new event
        Event event = new Event();
        event.setSummary("walk 300km");
        event.setLocation("queens park");

        ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();
        attendees.add(new EventAttendee().setEmail("matteo.matteovich@gmail.com"));
        event.setAttendees(attendees);

        DateTime start = new DateTime(schedule.getStartDate(), TimeZone.getTimeZone("UTC"));
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(schedule.getEndDate(), TimeZone.getTimeZone("UTC"));
        event.setEnd(new EventDateTime().setDateTime(end));

       // Insert the new event
        Event createdEvent = service.events().insert("primary", event).execute();
    }

}
