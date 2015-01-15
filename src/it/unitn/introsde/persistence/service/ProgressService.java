package it.unitn.introsde.persistence.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.helpers.Progress;
import it.unitn.introsde.persistence.dao.GoalDao;
import it.unitn.introsde.persistence.dao.MeasureDao;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class ProgressService {

    private static final Logger logger = LogManager.getLogger();

    private PersonDao personDao;

    private MeasureDao measureDao;

    private GoalDao goalDao;

    @RequestMapping(value = "/progress/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Progress> progressPerson(@PathVariable("personId") int personId) {
        Person person = personDao.get(Person.class, personId);
        if (person == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Progress progress = new Progress("You", "Suck");
        logger.debug("Person progressed with=" + progress);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }

    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Autowired
    public void setGoalDao(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    @Autowired
    public void setMeasureDao(MeasureDao measureDao) {
        this.measureDao = measureDao;
    }
}

