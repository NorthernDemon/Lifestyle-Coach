package it.unitn.introsde.persistence.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.datasource.FaceBookDatasource;
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
        List<Person> people = personDao.getAll("FROM Person");
        if (people.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("People =" + people);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @RequestMapping(value = "/fbuser/{accesstoken}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> getFaceBookUser(@PathVariable("accesstoken") String accesstoken) {
        Person person;
        try {
            person = faceBookDatasource.getUser(accesstoken);
        } catch (ParseException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.debug("Person =" + person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @RequestMapping(value = "/getpersonbyid/{personId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> getPersonbyid( @PathVariable("personId") int personId) {
        Person person = personDao.get(Person.class,personId);
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
