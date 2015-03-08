package it.unitn.introsde.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.datasource.FaceBookDatasource;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class PersonService {

    private static final Logger logger = LogManager.getLogger();

    private PersonDao personDao;

    private FaceBookDatasource faceBookDatasource;

    @RequestMapping(value = "/person", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> createPerson(
            @Valid @RequestBody Person person,
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        personDao.save(person);
        logger.debug("Created person=" + person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @RequestMapping(value = "/people", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Person>> getPeople() {
        List<Person> people = personDao.list(Person.class);
        if (people.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("People =" + people);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @RequestMapping(value = "/fbUser/{accessToken}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> getFaceBookUser(
            @PathVariable("accessToken") String accessToken) {
        try {
            Person person = faceBookDatasource.getUser(accessToken);
            logger.debug("Person=" + person);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (ParseException e) {
            logger.error("Failed to parse the person", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getPersonById/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> getPersonById(
            @PathVariable("personId") int personId) {
        Person person = personDao.get(Person.class, personId);
        if (person == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("Person =" + person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Autowired
    public void setFaceBookDatasource(FaceBookDatasource faceBookDatasource) {
        this.faceBookDatasource = faceBookDatasource;
    }
}
