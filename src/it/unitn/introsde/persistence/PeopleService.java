package it.unitn.introsde.persistence;

import it.unitn.introsde.StandaloneClientLauncher;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = StandaloneClientLauncher.SERVER_STUDENT_NAME)
public class PeopleService {

    private PersonDao personDao;

    /**
     * Request #1: GET /person should list all the people (see above Person model to know what data to return here) in your database (wrapped under the root element "people")
     * <p/>
     * Extra #4 (Request #12): GET /person?measureType={measureType}&max={max}&min={min} retrieves people whose {measureType} (e.g., weight) value is in the [{min},{max}] range (if only one for the query params is provided, use only that)
     */
    @RequestMapping(value = {"/person", "/person?measureType={measureType}&max={max}&min={min}"}, method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPeople(@RequestHeader(value = "Accept") String accept,
                                       @RequestParam(value = "measureType", required = false) String measurementType,
                                       @RequestParam(value = "max", required = false) Double max,
                                       @RequestParam(value = "min", required = false) Double min) {
        if (measurementType != null && max != null && min != null) {
            List<Person> persons = personDao.findWithinMeasurementType(measurementType, max, min);
            switch (accept.toLowerCase()) {
                case MediaType.APPLICATION_XML_VALUE:
                    return new ResponseEntity<>(new People(persons), HttpStatus.OK);
                case MediaType.APPLICATION_JSON_VALUE:
                    if (StandaloneClientLauncher.JSON_UNWRAP_LIST) {
                        return new ResponseEntity<>(persons, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new People(persons), HttpStatus.OK);
                    }
                default:
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            List<Person> persons = personDao.list();
            switch (accept.toLowerCase()) {
                case MediaType.APPLICATION_XML_VALUE:
                    return new ResponseEntity<>(new People(persons), HttpStatus.OK);
                case MediaType.APPLICATION_JSON_VALUE:
                    if (StandaloneClientLauncher.JSON_UNWRAP_LIST) {
                        return new ResponseEntity<>(persons, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new People(persons), HttpStatus.OK);
                    }
                default:
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
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
    public Person updatePerson(@PathVariable("id") int id, @RequestBody Person person) {
        Person existingPerson = personDao.get(Person.class, id);
        existingPerson.setFirstName(person.getFirstName());
        existingPerson.setLastName(person.getLastName());
        existingPerson.setBirthDate(person.getBirthDate());
        return personDao.update(existingPerson);
    }

    /**
     * Request #4: POST /person should create a new person and return the newly created person with its assigned id (if a health profile is included, create also those measurements for the new person).
     */
    @RequestMapping(value = "/person", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public Person postPerson(@RequestBody Person person) {
        return personDao.save(person);
    }

    /**
     * Request #5: DELETE /person/{id} should delete the person identified by {id} from the system
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void deletePerson(@PathVariable("id") int id) {
        personDao.delete(personDao.load(Person.class, id));
    }

    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
