package it.unitn.introsde.persistence;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.dao.MeasureDao;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.Persons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class PeopleService {

    private PersonDao personDao;

    private MeasureDao measureDao;

    /**
     * Request #1: GET /person should list all the people (see above Person model to know what data to return here) in your database (wrapped under the root element "people")
     * <p/>
     * Extra #4 (Request #12): GET /person?measureType={measureType}&max={max}&min={min} retrieves people whose {measureType} (e.g., weight) value is in the [{min},{max}] range (if only one for the query params is provided, use only that)
     */
    @RequestMapping(value = {"/person", "/person?measureType={measureType}&max={max}&min={min}"}, method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public Persons getPeople(@RequestParam(value = "measureType", required = false) String measurementType,
                             @RequestParam(value = "max", required = false) String max,
                             @RequestParam(value = "min", required = false) String min) {
        List<Person> persons;
        if (measurementType != null && max != null && min != null) {
            persons = measureDao.findPeopleByMeasureTypeWithinRange(measurementType, max, min);
        } else {
            persons = personDao.list();
        }
        for (Person person : persons) {
            person.setCurrentHealth(measureDao.findCurrentMeasureByPerson(person));
        }
        return new Persons(persons);
    }

    /**
     * Request #2: GET /person/{id} should give all the personal information plus current measures of person identified by {id} (e.g., current measures means current health profile)
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Person> getPerson(@PathVariable("id") int id) {
        Person person = personDao.get(Person.class, id);
        if (person != null) {
            person.setCurrentHealth(measureDao.findCurrentMeasureByPerson(person));
            return new ResponseEntity<>(person, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Request #3: PUT /person/{id} should update the personal information of the person identified by {id} (e.g., only the person's information, not the measures of the health profile)
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Person> updatePerson(@PathVariable("id") int id, @RequestBody Person person) {
        Person existingPerson = personDao.get(Person.class, id);
        if (existingPerson != null) {
            personDao.update(person);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Request #4: POST /person should create a new person and return the newly created person with its assigned id (if a health profile is included, create also those measurements for the new person).
     */
    @RequestMapping(value = "/person", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public Person savePerson(@RequestBody Person person) {
        Person savedPerson = personDao.save(person);
        for (Measure measure : savedPerson.getCurrentHealth()) {
            measure.setPerson(savedPerson);
            measureDao.save(measure);
        }
        return savedPerson;
    }

    /**
     * Request #5: DELETE /person/{id} should delete the person identified by {id} from the system
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePerson(@PathVariable("id") int id) {
        Person person = personDao.load(Person.class, id);
        measureDao.deleteByPerson(person);
        personDao.delete(person);
    }

    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Autowired
    public void setMeasureDao(MeasureDao measureDao) {
        this.measureDao = measureDao;
    }
}
