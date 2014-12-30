package it.unitn.introsde.persistence.process;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
            @RequestBody Goal goal) {
        logger.debug("Incoming [goal-process] with accept=" + accept + ", goal=" + goal);
        String url = ServiceConfiguration.getUrl() + "/goal";
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

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
