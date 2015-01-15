package it.unitn.introsde.persistence.process;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.helpers.Awareness;
import it.unitn.introsde.helpers.Motivation;
import it.unitn.introsde.helpers.Progress;
import it.unitn.introsde.helpers.Workout;
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

    @RequestMapping(value = "/motivation-process/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Motivation> motivatePerson(
            @RequestHeader(value = "Accept") String accept,
            @PathVariable("personId") int personId) {
        logger.debug("Incoming [motivation-process] with accept=" + accept + ", personId=" + personId);
        String url = ServiceConfiguration.getUrl() + "/motivation/" + personId;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept, null), Motivation.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Motivation motivation = (Motivation) exchange.getBody();
            logger.debug("Outgoing [motivation-process] with accept=" + accept + ", motivation=" + motivation);
            return new ResponseEntity<>(motivation, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [motivation-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/awareness-process/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Awareness> awarePerson(
            @RequestHeader(value = "Accept") String accept,
            @PathVariable("personId") int personId) {
        logger.debug("Incoming [awareness-process] with accept=" + accept + ", personId=" + personId);
        String url = ServiceConfiguration.getUrl() + "/awareness/" + personId;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept, null), Awareness.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Awareness awareness = (Awareness) exchange.getBody();
            logger.debug("Outgoing [awareness-process] with accept=" + accept + ", awareness=" + awareness);
            return new ResponseEntity<>(awareness, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [awareness-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/progress-process/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Progress> progressPerson(
            @RequestHeader(value = "Accept") String accept,
            @PathVariable("personId") int personId) {
        logger.debug("Incoming [progress-process] with accept=" + accept + ", personId=" + personId);
        String url = ServiceConfiguration.getUrl() + "/progress/" + personId;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept, null), Progress.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            Progress progress = (Progress) exchange.getBody();
            logger.debug("Outgoing [progress-process] with accept=" + accept + ", progress=" + progress);
            return new ResponseEntity<>(progress, exchange.getStatusCode());
        } else {
            logger.debug("Outgoing [progress-process] with accept=" + accept + ", statusCode=" + exchange.getStatusCode());
            return new ResponseEntity<>(exchange.getStatusCode());
        }
    }

    @RequestMapping(value = "/workout-process/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Workout> workoutPerson(
            @RequestHeader(value = "Accept") String accept,
            @PathVariable("personId") int personId) {
        logger.debug("Incoming [workout-process] with accept=" + accept + ", personId=" + personId);
        String url = ServiceConfiguration.getUrl() + "/workout/" + personId;
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.GET, createHeader(accept, null), Workout.class);
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
