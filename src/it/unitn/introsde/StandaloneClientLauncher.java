package it.unitn.introsde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unitn.introsde.helpers.Motivation;
import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.MeasureType;
import it.unitn.introsde.persistence.entity.Person;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public final class StandaloneClientLauncher {

    private static int id = 200500;

    private static final RestTemplate restTemplate = new RestTemplate();

    private static File file;

    private static MediaType applicationType;
    private static ObjectWriter mapper;

    private static HttpMethod httpMethod;
    private static String url;

    public static void main(String args[]) throws Exception {
        file = new File(ServiceConfiguration.getUrl().replace("/", "_").replace(":", "-").replace(".", "_") + ".txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        doWork(new XmlMapper().writerWithDefaultPrettyPrinter(), MediaType.APPLICATION_XML);
        doWork(new ObjectMapper().writerWithDefaultPrettyPrinter(), MediaType.APPLICATION_JSON);
    }

    private static HttpEntity<Object> createHeader(Object body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(applicationType));
        httpHeaders.setContentType(applicationType);
        return new HttpEntity<>(body, httpHeaders);
    }

    private static void logRequest(int requestNumber, boolean isOK, ResponseEntity<?> exchange) throws Exception {
        logRequest(requestNumber, isOK, mapper.writeValueAsString(exchange.getBody()), exchange.getStatusCode().toString(), exchange.getHeaders().getContentType().toString());
    }

    private static void logRequest(int requestNumber, boolean isOK, String body, String statusCode, String contentType) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request #").append(requestNumber).append(": ").append(httpMethod).append(" ").append(url).append(" Accept: ").append(contentType).append(" Content-type: ").append(contentType);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("=> Result: ").append(isOK ? "OK" : "ERROR");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("=> HTTP Status: ").append(statusCode);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(body);
        stringBuilder.append(System.getProperty("line.separator"));

        System.out.println(stringBuilder.toString());
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.append(stringBuilder);
        }
    }

    private static void doWork(ObjectWriter objectWriter, MediaType contentType) throws Exception {
        mapper = objectWriter;
        applicationType = contentType;
        ResponseEntity<?> exchange;

        MeasureType measureType = new MeasureType("height", "kilometers");

        httpMethod = HttpMethod.POST;
        url = ServiceConfiguration.getUrl() + "/person-process";
        Person person = new Person("Chuck", "Norris", getDate(1945, 0, 1), id++, id++);
        exchange = restTemplate.exchange(url, httpMethod, createHeader(person), Person.class);
        logRequest(0, exchange.getStatusCode().is2xxSuccessful(), exchange);
        person = (Person) exchange.getBody();

        httpMethod = HttpMethod.POST;
        url = ServiceConfiguration.getUrl() + "/goal-process";
        Goal goal = new Goal(person, person, measureType, 72.0, "You can do it!", getDate(2014, 5, 10), getDate(2014, 5, 15));
        exchange = restTemplate.exchange(url, httpMethod, createHeader(goal), Goal.class);
        logRequest(0, exchange.getStatusCode().is2xxSuccessful(), exchange);

        httpMethod = HttpMethod.POST;
        url = ServiceConfiguration.getUrl() + "/measure-process";
        Measure measure = new Measure(person, measureType, 72.1, getDate(2014, 5, 12));
        exchange = restTemplate.exchange(url, httpMethod, createHeader(measure), Measure.class);
        logRequest(0, exchange.getStatusCode().is2xxSuccessful(), exchange);

        httpMethod = HttpMethod.POST;
        url = ServiceConfiguration.getUrl() + "/motivation-process";
        exchange = restTemplate.exchange(url, httpMethod, createHeader(person), Motivation.class);
        logRequest(0, exchange.getStatusCode().is2xxSuccessful(), exchange);
    }

    /**
     * Because Java has fantastic Date API, we have to either use JodaTime or Calendar
     *
     * @param year  of the date
     * @param month of the year [0..11]
     * @param day   of the month [1..31|30|29|28]
     * @return date of given year, month and day
     */
    private static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }
}
