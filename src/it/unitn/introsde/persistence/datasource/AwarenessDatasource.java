package it.unitn.introsde.persistence.datasource;

import it.unitn.introsde.helpers.Awareness;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class AwarenessDatasource {

    private static final Logger logger = LogManager.getLogger();

    public Awareness getAwareness(Person person) {
        return new Awareness("severe", person.getName() + ", you gonna die.");
    }
}
