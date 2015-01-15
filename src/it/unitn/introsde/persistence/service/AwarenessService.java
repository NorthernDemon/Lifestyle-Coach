package it.unitn.introsde.persistence.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.helpers.Awareness;
import it.unitn.introsde.persistence.datasource.AwarenessDatasource;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class AwarenessService {

    private static final Logger logger = LogManager.getLogger();

    private AwarenessDatasource awarenessDatasource;

    @RequestMapping(value = "/awareness", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Awareness> awarePerson(
            @Valid @RequestBody Person person,
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Awareness awareness = awarenessDatasource.getAwareness(person);
        logger.debug("Person awared with=" + awareness);
        return new ResponseEntity<>(awareness, HttpStatus.OK);
    }

    @Autowired
    public void setAwarenessDatasource(AwarenessDatasource awarenessDatasource) {
        this.awarenessDatasource = awarenessDatasource;
    }
}

