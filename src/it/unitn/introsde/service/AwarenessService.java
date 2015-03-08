package it.unitn.introsde.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.datasource.AwarenessDatasource;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.Awareness;
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
public class AwarenessService {

    private static final Logger logger = LogManager.getLogger();

    private PersonDao personDao;

    private AwarenessDatasource awarenessDatasource;

    @RequestMapping(value = "/awareness/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Awareness> awarePerson(@PathVariable("personId") int personId) {
        Person person = personDao.get(Person.class, personId);
        if (person == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Awareness awareness = awarenessDatasource.getAwareness(person);
        if (awareness == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("Person awareness=" + awareness);
        return new ResponseEntity<>(awareness, HttpStatus.OK);
    }

    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Autowired
    public void setAwarenessDatasource(AwarenessDatasource awarenessDatasource) {
        this.awarenessDatasource = awarenessDatasource;
    }
}

