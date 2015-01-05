package it.unitn.introsde.persistence.datasource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;

import java.io.File;
import java.util.Collections;

public class GoogleDatasource {

    // TODO mock up Google Calendar
    // this works as service connection
    // we need JSF to get user to expose his calendar to us
    // that will require different type of OAuth
    // Go to google dev console: https://console.developers.google.com
    //      Create Client ID
    //          APPLICATION TYPE
    //              Web application
    //              Accessed by web browsers over a network.

    // LOGIN with G+ account (similar to Facebook)
    // - https://developers.google.com/+/web/signin/

    // https://developers.google.com/google-apps/calendar/auth
    // Docs for service:
    // - https://developers.google.com/accounts/docs/OAuth2ServiceAccount
    // Docs for web-app:
    // - https://developers.google.com/accounts/docs/OAuth2WebServer
    // - https://code.google.com/p/google-api-java-client/wiki/OAuth2

    // All Calendar REST API:
    // - https://developers.google.com/google-apps/calendar/firstapp
    public static void main(String args[]) throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(jsonFactory)
                .setServiceAccountId("815360076208-m9vi0hcjq2q1uqgku2e8rk856qjvrv47@developer.gserviceaccount.com")
                .setServiceAccountPrivateKeyFromP12File(new File("My Project-6fdeb93221d2.p12"))
                .setServiceAccountScopes(Collections.singleton("https://www.googleapis.com/auth/calendar"))
                .build();

        Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credential).setApplicationName("LifestyleCoach").build();
        Calendar.CalendarList.List list = service.calendarList().list();
        CalendarList calendars = list.execute();
        System.out.println(calendars.toPrettyString());
        for (Object calendarListEntry : calendars.getItems()) {
            System.out.println(calendarListEntry);
        }
        HttpResponse httpResponse = list.executeUsingHead();
        System.out.println(httpResponse.getContentType());
        System.out.println(httpResponse.getStatusMessage());
    }
}
