package it.unitn.introsde.persistence.service;

import it.unitn.introsde.BeanValidationException;
import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class PersonService {

    private PersonDao personDao;

    @RequestMapping(value = "/person", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> createProfile(@Valid @RequestBody Person person, BindingResult result) {
        if (result.hasErrors()) {
            throw new BeanValidationException(result.getAllErrors());
        }
        personDao.save(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
