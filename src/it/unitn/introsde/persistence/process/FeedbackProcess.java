package it.unitn.introsde.persistence.process;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.helpers.Awareness;
import it.unitn.introsde.helpers.Motivation;
import it.unitn.introsde.helpers.Progress;
import it.unitn.introsde.helpers.Workout;
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
public class FeedbackProcess extends AbstractProcess {

    private static final Logger logger = LogManager.getLogger();

    private RestTemplate restTemplate;

    @RequestMapping(value = "/motivation-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Motivation> motivatePerson(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Person person) {
        logger.debug("Incoming [motivation-process] with accept=" + accept + ", person=" + person);
        String url = ServiceConfiguration.getUrl() + "/motivation";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, person), Motivation.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Motivation motivation = (Motivation) exchange.getBody();
            logger.debug("Outgoing [motivation-process] with accept=" + accept + ", motivation=" + motivation);
            return new ResponseEntity<>(motivation, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [motivation-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/awareness-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Awareness> awarePerson(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Person person) {
        logger.debug("Incoming [awareness-process] with accept=" + accept + ", person=" + person);
        String url = ServiceConfiguration.getUrl() + "/awareness";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, person), Awareness.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Awareness awareness = (Awareness) exchange.getBody();
            logger.debug("Outgoing [awareness-process] with accept=" + accept + ", awareness=" + awareness);
            return new ResponseEntity<>(awareness, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [awareness-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/progress-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Progress> progressPerson(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Person person) {
        logger.debug("Incoming [progress-process] with accept=" + accept + ", person=" + person);
        String url = ServiceConfiguration.getUrl() + "/progress";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, person), Progress.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Progress progress = (Progress) exchange.getBody();
            logger.debug("Outgoing [progress-process] with accept=" + accept + ", progress=" + progress);
            return new ResponseEntity<>(progress, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [progress-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/workout-process", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Workout> workoutPerson(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Person person) {
        logger.debug("Incoming [workout-process] with accept=" + accept + ", person=" + person);
        String url = ServiceConfiguration.getUrl() + "/workout";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, person), Workout.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Workout workout = (Workout) exchange.getBody();
            logger.debug("Outgoing [workout-process] with accept=" + accept + ", workout=" + workout);
            return new ResponseEntity<>(workout, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [workout-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
