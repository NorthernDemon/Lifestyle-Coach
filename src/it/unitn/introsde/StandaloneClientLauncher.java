package it.unitn.introsde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.Measures;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        /**** TEST *****/

        httpMethod = HttpMethod.POST;
        url = ServiceConfiguration.getUrl() + "/profile";
        Person chuckNorris = new Person("Chuck", "Norris", getDate(1945, 0, 1), id++, id++);
        exchange = restTemplate.exchange(url, httpMethod, createHeader(chuckNorris), Person.class);
        logRequest(0, exchange.getStatusCode().is2xxSuccessful(), exchange);

        /**** TEST *****/
//
//        /**
//         * Step 3.1. Send R#1 (GET BASE_URL/person). Calculate how many people are in the response.
//         * If more than 2, result is OK, else is ERROR (less than 3 persons).
//         * Save into a variable id of the first person (first_person_id) and of the last person (last_person_id)
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/person";
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Persons.class);
//        List<Person> persons = ((Persons) exchange.getBody()).getPersons();
//        int personCount = persons.size();
//        Person firstPerson = persons.get(0);
//        Person lastPerson = persons.get(persons.size() - 1);
//        logRequest(1, personCount > 2, exchange);
//
//        /**
//         * Step 3.2. Send R#2 for first_person_id. If the responses for this is 200 or 202, the result is OK.
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/person/" + firstPerson.getId();
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Person.class);
//        logRequest(2, exchange.getStatusCode().is2xxSuccessful(), exchange);
//
//        /**
//         * Step 3.3. Send R#3 for first_person_id changing the firstname. If the responses has the name changed, the result is OK.
//         */
//        httpMethod = HttpMethod.PUT;
//        url = ServiceConfiguration.getUrl() + "/person/" + firstPerson.getId();
//        String updatedFirstName = "Victor";
//        Person updatedPerson = (Person) exchange.getBody();
//        updatedPerson.setName(updatedFirstName);
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(updatedPerson), Person.class);
//        logRequest(3, updatedFirstName.equals(((Person) exchange.getBody()).getName()), exchange);
//
//        /**
//         * Step 3.4. Send R#4 to create the following person (first using JSON and then using XML). Store the id of the new person.
//         * If the answer is 201 (200 or 202 are also applicable) with a person in the body who has an ID, the result is OK.
//         */
//        httpMethod = HttpMethod.POST;
//        url = ServiceConfiguration.getUrl() + "/person";
//        Person chuckNorris = new Person("Chuck", "Norris", getDate(1945, 0, 1));
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(chuckNorris), Person.class);
//        chuckNorris = (Person) exchange.getBody();
//        logRequest(4, exchange.getStatusCode().is2xxSuccessful(), exchange);
//
//        /**
//         * Step 3.5. Send R#5 for the person you have just created.
//         */
//        httpMethod = HttpMethod.DELETE;
//        url = ServiceConfiguration.getUrl() + "/person/" + chuckNorris.getId();
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Person.class);
//        logRequest(5, exchange.getStatusCode().is2xxSuccessful(), "", exchange.getStatusCode().toString(), applicationType.toString());
//
//        /**
//         * Then send R#1 with the id of that person. If the answer is 404, your result must be OK.
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/person/" + chuckNorris.getId();
//        try {
//            exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Person.class);
//            logRequest(1, false, exchange);
//        } catch (HttpClientErrorException e) {
//            logRequest(1, true, "", "404", applicationType.toString());
//        }
//
//        /**
//         * Step 3.6. Follow now with the R#9 (GET BASE_URL/measureTypes).
//         * If response contains more than 2 measureTypes - result is OK, else is ERROR (less than 3 measureTypes).
//         * Save all measureTypes into array (measure_types)
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/measureTypes";
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), MeasureTypes.class);
//        List<String> measurementTypesList = ((MeasureTypes) exchange.getBody()).getMeasureTypes();
//        logRequest(9, measurementTypesList.size() >= 2, exchange);
//        printMeasurements(firstPerson.getId(), measurementTypesList);
//        Measure measure = printMeasurements(lastPerson.getId(), measurementTypesList);
//        String measureType = measurementTypesList.get(measurementTypesList.size() - 1);
//
//        /**
//         * Step 3.8. Send R#7 (GET BASE_URL/person/{id}/{measureType}/{mid}) for the stored measure_id and measureType.
//         * If the response is 200, result is OK, else is ERROR.
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/person/" + lastPerson.getId() + '/' + measureType + '/' + measure.getId();
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Measure.class);
//        logRequest(7, exchange.getStatusCode().is2xxSuccessful(), exchange);
//
//        /**
//         * Step 3.9. Choose a measureType from measure_types and send the request R#6 (GET BASE_URL/person/{first_person_id}/{measureType})
//         * and save count value (e.g. 5 measurements).
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/person/" + firstPerson.getId() + '/' + measureType;
//        System.out.print(url);
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Measures.class);
//        int countFirstPersonMeasurements = ((Measures) exchange.getBody()).getMeasures().size();
//        logRequest(6, exchange.getStatusCode().is2xxSuccessful(), exchange);
//
//        /**
//         * Then send R#8 (POST BASE_URL/person/{first_person_id}/{measureTypes}) with the measurement specified below.
//         */
//        httpMethod = HttpMethod.POST;
//        url = ServiceConfiguration.getUrl() + "/person/" + firstPerson.getId() + '/' + measureType;
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(new Measure(firstPerson, new MeasureType("height", "meters"), 72.0)), Measure.class);
//        Measure savedHealthHistory = (Measure) exchange.getBody();
//        logRequest(8, exchange.getStatusCode().is2xxSuccessful(), exchange);
//
//        /**
//         * Follow up with another R#6 as the first to check the new count value.
//         * If it is 1 measure more - print OK, else print ERROR. Remember, first with JSON and then with XML as content-types
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/person/" + firstPerson.getId() + '/' + measureType;
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Measures.class);
//        logRequest(6, ((Measures) exchange.getBody()).getMeasures().size() - 1 == countFirstPersonMeasurements, exchange);
//
//        /**
//         * Step 3.10. Send R#10 using the {mid} or the measure created in the previous step and updating the value at will.
//         */
//        httpMethod = HttpMethod.PUT;
//        url = ServiceConfiguration.getUrl() + "/person/" + firstPerson.getId() + '/' + measureType + '/' + savedHealthHistory.getId();
//        savedHealthHistory.setValue(90);
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(savedHealthHistory), Measure.class);
//        logRequest(10, exchange.getStatusCode().is2xxSuccessful(), exchange);
//
//        /**
//         * Follow up with at R#7 to check that the value was updated. If it was, result is OK, else is ERROR.
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/person/" + firstPerson.getId() + '/' + measureType + '/' + savedHealthHistory.getId();
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Measure.class);
//        logRequest(7, exchange.getBody().equals(savedHealthHistory), exchange);
//
//        /**
//         * Step 3.11. Send R#11 for a measureType, before and after dates given by your fellow student (who implemnted the server).
//         * If status is 200 and there is at least one measure in the body, result is OK, else is ERROR
//         */
//        httpMethod = HttpMethod.GET;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        url = ServiceConfiguration.getUrl() + "/person/" + firstPerson.getId() + '/' + measureType + "?before=" + dateFormat.format(new Date()) + "&after=" + dateFormat.format(getDate(2012, 0, 1));
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Measures.class);
//        logRequest(11, ((Measures) exchange.getBody()).getMeasures().size() > 1, exchange);
//
//        /**
//         * Step 3.12. Send R#12 using the same parameters as the preivious steps.
//         * If status is 200 and there is at least one person in the body, result is OK, else is ERROR
//         */
//        httpMethod = HttpMethod.GET;
//        url = ServiceConfiguration.getUrl() + "/person?measureType=" + measureType + "&max=" + 200.0 + "&min=" + 100.0;
//        exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Persons.class);
//        personCount = ((Persons) exchange.getBody()).getPersons().size();
//        logRequest(12, personCount > 1, exchange);
    }

    /**
     * Step 3.7. Send R#6 (GET BASE_URL/person/{id}/{measureType})
     * for the first person you obtained at the beginning and the last person,and for each measure types from measure_types.
     * If no response has at least one measure - result is ERROR (no data at all) else result is OK. Store one measure_id and one measureType.
     */
    private static Measure printMeasurements(int personId, List<String> measureType) throws Exception {
        httpMethod = HttpMethod.GET;
        Measure measure = null;
        for (String type : measureType) {
            url = ServiceConfiguration.getUrl() + "/person/" + personId + '/' + type;
            ResponseEntity<?> exchange = restTemplate.exchange(url, httpMethod, createHeader(""), Measures.class);
            measure = ((Measures) exchange.getBody()).getMeasures().get(0);
            logRequest(6, exchange.getStatusCode().is2xxSuccessful(), exchange);
        }
        return measure;
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
