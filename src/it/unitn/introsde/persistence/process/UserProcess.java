package it.unitn.introsde.persistence.process;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.MeasureType;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.Schedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class UserProcess extends AbstractProcess {

    private static final Logger logger = LogManager.getLogger();

    private RestTemplate restTemplate;

    @RequestMapping(value = "/person-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> createPerson(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Person person) {
        logger.debug("Incoming [person-process] with accept=" + accept + ", person=" + person);
        String url = ServiceConfiguration.getUrl() + "/person";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, person), Person.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Person createdPerson = (Person) exchange.getBody();
            logger.debug("Outgoing [person-process] with accept=" + accept + ", person=" + createdPerson);
            return new ResponseEntity<>(createdPerson, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [person-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/goal-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Goal> createGoal(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Goal goal,
            @RequestParam(value = "fbAccessToken") String fbAccessToken) {
        logger.debug("Incoming [goal-process] with accept=" + accept + ", goal=" + goal);
        String url = ServiceConfiguration.getUrl() + "/goal?fbAccessToken=" + fbAccessToken;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, goal), Goal.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Goal createdGoal = (Goal) exchange.getBody();
            logger.debug("Outgoing [goal-process] with accept=" + accept + ", goal=" + createdGoal);
            return new ResponseEntity<>(createdGoal, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [goal-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/measure-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Measure> createMeasure(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Measure measure) {
        logger.debug("Incoming [measure-process] with accept=" + accept + ", measure=" + measure);
        String url = ServiceConfiguration.getUrl() + "/measure";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, measure), Measure.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Measure createdMeasure = (Measure) exchange.getBody();
            logger.debug("Outgoing [measure-process] with accept=" + accept + ", measure=" + createdMeasure);
            return new ResponseEntity<>(createdMeasure, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [measure-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/schedule-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Schedule> createSchedule(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Schedule schedule,
            @RequestParam(value = "googleAccessToken") String googleAccessToken) {
        logger.debug("Incoming [schedule-process] with accept=" + accept + ", schedule=" + schedule);
        String url = ServiceConfiguration.getUrl() + "/schedule?googleAccessToken=" + googleAccessToken;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, schedule), Schedule.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Schedule createdSchedule = (Schedule) exchange.getBody();
            logger.debug("Outgoing [schedule-process] with accept=" + accept + ", schedule=" + createdSchedule);
            return new ResponseEntity<>(createdSchedule, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [schedule-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/measureType-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<MeasureType> createMeasureType(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody MeasureType measureType) {
        logger.debug("Incoming [measureType-process] with accept=" + accept + ", measureType=" + measureType);
        String url = ServiceConfiguration.getUrl() + "/measureType";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, measureType), MeasureType.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            MeasureType createdMeasureType = (MeasureType) exchange.getBody();
            logger.debug("Outgoing [measureType-process] with accept=" + accept + ", measureType=" + createdMeasureType);
            return new ResponseEntity<>(createdMeasureType, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [measureType-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/people-process", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Person>> getPeople(
            @RequestHeader(value = "Accept") String accept) {
        logger.debug("Incoming [people-process] with accept=" + accept);
        String url = ServiceConfiguration.getUrl() + "/people";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept), List.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            @SuppressWarnings("unchecked")
            List<Person> people = (List<Person>) exchange.getBody();
            logger.debug("Outgoing [people-process] with accept=" + accept + ", people=" + people);
            return new ResponseEntity<>(people, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [people-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/fbUser-process/{accessToken}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> getFBUser(
            @RequestHeader(value = "Accept") String accept,
            @PathVariable("accessToken") String accessToken) {
        logger.debug("Incoming [fbUser-process] with accept=" + accept);
        String url = ServiceConfiguration.getUrl() + "/fbUser/" + accessToken;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept), Person.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Person person = (Person) exchange.getBody();
            logger.debug("Outgoing [fbUser-process] with accept=" + accept + ", person=" + person);
            return new ResponseEntity<>(person, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [fbUser-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/event-process", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Schedule>> getEvents(
            @RequestHeader(value = "Accept") String accept,
            @RequestParam(value = "accessToken") String accessToken) {
        logger.debug("Incoming [event-process] with accept=" + accept);
        String url = ServiceConfiguration.getUrl() + "/event?accessToken=" + accessToken;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept), List.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            @SuppressWarnings("unchecked")
            List<Schedule> events = (List<Schedule>) exchange.getBody();
            logger.debug("Outgoing [event-process] with accept=" + accept + ", events=" + events);
            return new ResponseEntity<>(events, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [event-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/measureTypes-process", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<MeasureType>> getMeasureTypes(
            @RequestHeader(value = "Accept") String accept) {
        logger.debug("Incoming [measureTypes-process] with accept=" + accept);
        String url = ServiceConfiguration.getUrl() + "/measureTypes";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept), List.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            @SuppressWarnings("unchecked")
            List<MeasureType> measureTypes = (List<MeasureType>) exchange.getBody();
            logger.debug("Outgoing [measureTypes-process] with accept=" + accept + ", measureTypes=" + measureTypes);
            return new ResponseEntity<>(measureTypes, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [measureTypes-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/getPersonById-process/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> getPersonById(
            @RequestHeader(value = "Accept") String accept,
            @PathVariable("personId") int personId) {
        logger.debug("Incoming [getPersonById-process] with accept=" + accept + ", personId=" + personId);
        String url = ServiceConfiguration.getUrl() + "/getPersonById/" + personId;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept), Person.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Person person = (Person) exchange.getBody();
            logger.debug("Outgoing [getPersonById-process] with accept=" + accept + ", person=" + person);
            return new ResponseEntity<>(person, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [getPersonById-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/getMeasureTypeById-process/{measureTypeId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<MeasureType> getMeasureTypeById(
            @RequestHeader(value = "Accept") String accept,
            @PathVariable("measureTypeId") int measureTypeId) {
        logger.debug("Incoming [getMeasureTypeById-process] with accept=" + accept + ", measureTypeId=" + measureTypeId);
        String url = ServiceConfiguration.getUrl() + "/getMeasureTypeById/" + measureTypeId;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept), MeasureType.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            MeasureType measureType = (MeasureType) exchange.getBody();
            logger.debug("Outgoing [getMeasureTypeById-process] with accept=" + accept + ", measureType=" + measureType);
            return new ResponseEntity<>(measureType, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [getMeasureTypeById-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
