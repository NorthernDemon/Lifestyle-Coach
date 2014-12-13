package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.Person;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PersonDao extends AbstractDao<Person> {

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Person> findWithinMeasurementType(String measurementType, double max, double min) {
        Query namedQuery = getSession().getNamedQuery(getString(measurementType));
        namedQuery.setParameter("max", max);
        namedQuery.setParameter("min", min);
        return namedQuery.list();
    }

    private String getString(String measurementType) {
        switch (measurementType) {
            case "height":
                return Person.FIND_WITHIN_MEASUREMENT_TYPE_HEIGHT;
            case "weight":
                return Person.FIND_WITHIN_MEASUREMENT_TYPE_WEIGHT;
            case "steps":
                return Person.FIND_WITHIN_MEASUREMENT_TYPE_STEPS;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Person> list() {
        return getSession().createCriteria(Person.class).list();
    }
}