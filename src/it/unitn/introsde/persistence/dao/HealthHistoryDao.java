package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.HealthHistory;
import it.unitn.introsde.persistence.entity.Person;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public class HealthHistoryDao extends AbstractDao<HealthHistory> {

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<HealthHistory> findByPersonAndMeasurementType(Person person, String measurementType) {
        Query namedQuery = getSession().getNamedQuery(HealthHistory.FIND_BY_PERSON_AND_MEASUREMENT_TYPE);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("measurementType", measurementType);
        return namedQuery.list();
    }

    @Transactional(readOnly = true)
    public HealthHistory findByPersonAndMeasurementTypeAndId(Person person, String measurementType, int id) {
        Query namedQuery = getSession().getNamedQuery(HealthHistory.FIND_BY_PERSON_AND_MEASUREMENT_TYPE_AND_ID);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("measurementType", measurementType);
        namedQuery.setParameter("id", id);
        return (HealthHistory) namedQuery.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<HealthHistory> findByPersonAndMeasurementTypeWithinDates(Person person, String measurementType, Date afterDate, Date beforeDate) {
        Query namedQuery = getSession().getNamedQuery(HealthHistory.FIND_BY_PERSON_AND_MEASUREMENT_TYPE_WITHIN_DATES);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("measurementType", measurementType);
        namedQuery.setParameter("afterDate", afterDate);
        namedQuery.setParameter("beforeDate", beforeDate);
        return namedQuery.list();
    }
}