package it.unitn.introsde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unitn.introsde.persistence.entity.HealthHistory;
import it.unitn.introsde.persistence.entity.HealthProfile;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.MeasurementHistory;
import it.unitn.introsde.wrapper.MeasurementType;
import it.unitn.introsde.wrapper.People;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class StandaloneClientLauncher {

    /**
     * TRUE for having parent element for JSON lists (lab7)
     * FALSE for unwrapping JSON list into array without parent element
     */
    public static final boolean JSON_UNWRAP_LIST = true;

    /**
     * SERVER IP ADDRESS
     */
    public static String IP;

    static {
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
//            IP = "10.25.157.80";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * SERVER PORT NUMBER
     */
    public static final int PORT = 443;

    /**
     * SERVER STUDENT NAME
     */
    public static final String SERVER_STUDENT_NAME = "/victorekimov";
//    public static final String SERVER_STUDENT_NAME = "/sdelab";

    private static final String BASE_URL = "http://" + IP + ":" + PORT + SERVER_STUDENT_NAME;

    private static final RestTemplate restTemplate = new RestTemplate();

    private static File file;

    private static MediaType applicationType;
    private static ObjectWriter mapper;

    private static HttpMethod httpMethod;
    private static String url;

    public static void main(String args[]) throws Exception {
        file = new File(SERVER_STUDENT_NAME.replace("/", "") + '_' + BASE_URL.replace(SERVER_STUDENT_NAME, "").replace(":", "-").replace("/", "_") + "_results.txt");
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

    /**
     * ACTUAL WORK
     */

    private static void doWork(ObjectWriter objectWriter, MediaType contentType) throws Exception {
        mapper = objectWriter;
        applicationType = contentType;
        ResponseEntity<?> exchange;

        /**
         * Step 3.1. Send R#1 (GET BASE_URL/person). Calculate how many people are in the response.
         * If more than 2, result is OK, else is ERROR (less than 3 persons).
         * Save into a variable id of the first person (first_person_id) and of the last person (last_person_id)
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/person";
        List<Person> persons = null;
        if (JSON_UNWRAP_LIST && MediaType.APPLICATION_JSON_VALUE.equals(applicationType.toString())) {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Person[].class);
            persons = Arrays.asList((Person[]) exchange.getBody());
        } else {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), People.class);
            persons = ((People) exchange.getBody()).getPersons();
        }
        int personCount = persons.size();
        Person firstPerson = persons.get(0);
        Person lastPerson = persons.get(persons.size() - 1);
        logRequest(1, personCount > 2, exchange);

        /**
         * Step 3.2. Send R#2 for first_person_id. If the responses for this is 200 or 202, the result is OK.
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/person/" + firstPerson.getId();
        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Person.class);
        logRequest(2, exchange.getStatusCode().is2xxSuccessful(), exchange);

        /**
         * Step 3.3. Send R#3 for first_person_id changing the firstname. If the responses has the name changed, the result is OK.
         */
        httpMethod = HttpMethod.PUT;
        url = BASE_URL + "/person/" + firstPerson.getId();
        String updatedFirstName = "Victor";
        Person updatedPerson = (Person) exchange.getBody();
        updatedPerson.setFirstName(updatedFirstName);
        exchange = restTemplate.exchange(url, httpMethod, createHeader(updatedPerson), Person.class);
        logRequest(3, updatedFirstName.equals(((Person) exchange.getBody()).getFirstName()), exchange);

        /**
         * Step 3.4. Send R#4 to create the following person (first using JSON and then using XML). Store the id of the new person.
         * If the answer is 201 (200 or 202 are also applicable) with a person in the body who has an ID, the result is OK.
         */
        httpMethod = HttpMethod.POST;
        url = BASE_URL + "/person";
        exchange = restTemplate.exchange(url, httpMethod, createHeader(new Person("Chuck", "Norris", getDate(1945, 0, 1), new HealthProfile(78.9, 172.0, 666.0))), Person.class);
        Person chuckNorris = (Person) exchange.getBody();
        logRequest(4, exchange.getStatusCode().is2xxSuccessful(), exchange);

        /**
         * Step 3.5. Send R#5 for the person you have just created.
         */
        httpMethod = HttpMethod.DELETE;
        url = BASE_URL + "/person/" + chuckNorris.getId();
        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Person.class);
        logRequest(5, exchange.getStatusCode().is2xxSuccessful(), "", exchange.getStatusCode().toString(), applicationType.toString());

        /**
         * Then send R#1 with the id of that person. If the answer is 404, your result must be OK.
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/person/" + chuckNorris.getId();
        try {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Person.class);
            logRequest(1, false, exchange);
        } catch (HttpClientErrorException e) {
            logRequest(1, true, "", "404", applicationType.toString());
        }

        /**
         * Step 3.6. Follow now with the R#9 (GET BASE_URL/measureTypes).
         * If response contains more than 2 measureTypes - result is OK, else is ERROR (less than 3 measureTypes).
         * Save all measureTypes into array (measure_types)
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/measureTypes";
        List<String> measurementTypesList = null;
        if (JSON_UNWRAP_LIST && MediaType.APPLICATION_JSON_VALUE.equals(applicationType.toString())) {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), String[].class);
            measurementTypesList = Arrays.asList((String[]) exchange.getBody());
        } else {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), MeasurementType.class);
            measurementTypesList = ((MeasurementType) exchange.getBody()).getMeasurementTypes();
        }
        logRequest(9, measurementTypesList.size() > 2, exchange);
        printMeasurements(firstPerson.getId(), measurementTypesList);
        HealthHistory healthHistory = printMeasurements(lastPerson.getId(), measurementTypesList);
        String measureType = measurementTypesList.get(measurementTypesList.size() - 1);

        /**
         * Step 3.8. Send R#7 (GET BASE_URL/person/{id}/{measureType}/{mid}) for the stored measure_id and measureType.
         * If the response is 200, result is OK, else is ERROR.
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/person/" + lastPerson.getId() + '/' + measureType + '/' + healthHistory.getId();
        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), HealthHistory.class);
        logRequest(7, exchange.getStatusCode().is2xxSuccessful(), exchange);

        /**
         * Step 3.9. Choose a measureType from measure_types and send the request R#6 (GET BASE_URL/person/{first_person_id}/{measureType})
         * and save count value (e.g. 5 measurements).
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/person/" + firstPerson.getId() + '/' + measureType;
        int countFirstPersonMeasurements = 0;
        if (JSON_UNWRAP_LIST && MediaType.APPLICATION_JSON_VALUE.equals(applicationType.toString())) {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), HealthHistory[].class);
            countFirstPersonMeasurements = ((HealthHistory[]) exchange.getBody()).length;
            logRequest(6, exchange.getStatusCode().is2xxSuccessful(), exchange);
        } else {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), MeasurementHistory.class);
            countFirstPersonMeasurements = ((MeasurementHistory) exchange.getBody()).getMeasurementHistories().size();
            logRequest(6, exchange.getStatusCode().is2xxSuccessful(), exchange);
        }

        /**
         * Then send R#8 (POST BASE_URL/person/{first_person_id}/{measureTypes}) with the measurement specified below.
         */
        httpMethod = HttpMethod.POST;
        url = BASE_URL + "/person/" + firstPerson.getId() + '/' + measureType;
        exchange = restTemplate.exchange(url, httpMethod, createHeader(new HealthHistory(firstPerson, measureType, 72)), HealthHistory.class);
        HealthHistory savedHealthHistory = (HealthHistory) exchange.getBody();
        logRequest(8, exchange.getStatusCode().is2xxSuccessful(), exchange);

        /**
         * Follow up with another R#6 as the first to check the new count value.
         * If it is 1 measure more - print OK, else print ERROR. Remember, first with JSON and then with XML as content-types
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/person/" + firstPerson.getId() + '/' + measureType;
        if (JSON_UNWRAP_LIST && MediaType.APPLICATION_JSON_VALUE.equals(applicationType.toString())) {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), HealthHistory[].class);
            logRequest(6, ((HealthHistory[]) exchange.getBody()).length - 1 == countFirstPersonMeasurements, exchange);
        } else {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), MeasurementHistory.class);
            logRequest(6, ((MeasurementHistory) exchange.getBody()).getMeasurementHistories().size() - 1 == countFirstPersonMeasurements, exchange);
        }

        /**
         * Step 3.10. Send R#10 using the {mid} or the measure created in the previous step and updating the value at will.
         */
        httpMethod = HttpMethod.PUT;
        url = BASE_URL + "/person/" + firstPerson.getId() + '/' + measureType + '/' + savedHealthHistory.getId();
        savedHealthHistory.setValue(90);
        exchange = restTemplate.exchange(url, httpMethod, createHeader(savedHealthHistory), HealthHistory.class);
        logRequest(10, exchange.getStatusCode().is2xxSuccessful(), exchange);

        /**
         * Follow up with at R#7 to check that the value was updated. If it was, result is OK, else is ERROR.
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/person/" + firstPerson.getId() + '/' + measureType + '/' + savedHealthHistory.getId();
        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), HealthHistory.class);
        logRequest(7, exchange.getBody().equals(savedHealthHistory), exchange);

        /**
         * Step 3.11. Send R#11 for a measureType, before and after dates given by your fellow student (who implemnted the server).
         * If status is 200 and there is at least one measure in the body, result is OK, else is ERROR
         */
        httpMethod = HttpMethod.GET;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        url = BASE_URL + "/person/" + firstPerson.getId() + '/' + measureType + "?before=" + dateFormat.format(new Date()) + "&after=" + dateFormat.format(getDate(2012, 0, 1));
        if (JSON_UNWRAP_LIST && MediaType.APPLICATION_JSON_VALUE.equals(applicationType.toString())) {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), HealthHistory[].class);
            logRequest(11, ((HealthHistory[]) exchange.getBody()).length > 1, exchange);
        } else {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), MeasurementHistory.class);
            logRequest(11, ((MeasurementHistory) exchange.getBody()).getMeasurementHistories().size() > 1, exchange);
        }

        /**
         * Step 3.12. Send R#12 using the same parameters as the preivious steps.
         * If status is 200 and there is at least one person in the body, result is OK, else is ERROR
         */
        httpMethod = HttpMethod.GET;
        url = BASE_URL + "/person?measureType=" + measureType + "&max=" + 1000.0 + "&min=" + 500.0;
        if (JSON_UNWRAP_LIST && MediaType.APPLICATION_JSON_VALUE.equals(applicationType.toString())) {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Person[].class);
            personCount = ((Person[]) exchange.getBody()).length;
        } else {
            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), People.class);
            personCount = ((People) exchange.getBody()).getPersons().size();
        }
        logRequest(12, personCount > 1, exchange);
    }

    /**
     * Step 3.7. Send R#6 (GET BASE_URL/person/{id}/{measureType})
     * for the first person you obtained at the beginning and the last person,and for each measure types from measure_types.
     * If no response has at least one measure - result is ERROR (no data at all) else result is OK. Store one measure_id and one measureType.
     */
    private static HealthHistory printMeasurements(int personId, List<String> measurementType) throws Exception {
        httpMethod = HttpMethod.GET;
        HealthHistory healthHistory = null;
        for (String type : measurementType) {
            url = BASE_URL + "/person/" + personId + '/' + type;
            ResponseEntity<?> exchange;
            if (JSON_UNWRAP_LIST && MediaType.APPLICATION_JSON_VALUE.equals(applicationType.toString())) {
                exchange = restTemplate.exchange(url, httpMethod, createHeader(""), HealthHistory[].class);
                healthHistory = ((HealthHistory[]) exchange.getBody())[0];
                logRequest(6, exchange.getStatusCode().is2xxSuccessful(), exchange);
            } else {
                exchange = restTemplate.exchange(url, httpMethod, createHeader(""), MeasurementHistory.class);
                healthHistory = ((MeasurementHistory) exchange.getBody()).getMeasurementHistories().get(0);
                logRequest(6, exchange.getStatusCode().is2xxSuccessful(), exchange);
            }
        }
        return healthHistory;
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
