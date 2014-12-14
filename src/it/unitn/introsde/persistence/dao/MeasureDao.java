package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.Person;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public class MeasureDao extends AbstractDao<Measure> {

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Measure> findCurrentMeasureByPerson(Person person) {
        Query namedQuery = getSession().getNamedQuery(Measure.FIND_CURRENT_MEASURE_BY_PERSON);
        namedQuery.setParameter("person", person);
        return namedQuery.list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Measure> findHistoryMeasureByPersonAndMeasureType(Person person, String type) {
        Query namedQuery = getSession().getNamedQuery(Measure.FIND_HISTORY_MEASURE_BY_PERSON_AND_MEASURE_TYPE);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("type", type);
        return namedQuery.list();
    }

    @Transactional(readOnly = true)
    public Measure findByPersonAndMeasureTypeAndId(Person person, String type, int id) {
        Query namedQuery = getSession().getNamedQuery(Measure.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_ID);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("type", type);
        namedQuery.setParameter("id", id);
        return (Measure) namedQuery.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<String> findMeasureTypes() {
        return getSession().getNamedQuery(Measure.FIND_MEASURE_TYPES).list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Measure> findByPersonAndMeasurementTypeWithinDates(Person person, String type, Date after, Date before) {
        Query namedQuery = getSession().getNamedQuery(Measure.FIND_BY_PERSON_AND_MEASURE_TYPE_WITHIN_DATES);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("type", type);
        namedQuery.setParameter("after", after);
        namedQuery.setParameter("before", before);
        return namedQuery.list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Person> findPeopleByMeasureTypeWithinRange(String type, String max, String min) {
        Query namedQuery = getSession().getNamedQuery(Measure.FIND_PEOPLE_BY_MEASURE_TYPE_WITHIN_RANGE);
        namedQuery.setParameter("type", type);
        namedQuery.setParameter("min", min);
        namedQuery.setParameter("max", max);
        return namedQuery.list();
    }

    @Transactional
    public int deleteByPerson(Person person) {
        Query namedQuery = getSession().getNamedQuery(Measure.DELETE_BY_PERSON);
        namedQuery.setParameter("person", person);
        return namedQuery.executeUpdate();
    }
}