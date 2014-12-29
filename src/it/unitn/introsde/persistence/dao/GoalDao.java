package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.MeasureType;
import it.unitn.introsde.persistence.entity.Person;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Repository
public class GoalDao extends AbstractDao<Goal> {

    private MeasureTypeDao measureTypeDao;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Goal> findAccomplished(Person person, MeasureType measureType) {
        Query namedQuery = getSession().getNamedQuery(Goal.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_ACCOMPLISHED);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("measureType", measureType);
        return namedQuery.list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Goal> findCurrent(Person person, MeasureType measureType) {
        Query namedQuery = getSession().getNamedQuery(Goal.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_CURRENT);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("measureType", measureType);
        namedQuery.setParameter("currentTime", Calendar.getInstance().getTime());
        return namedQuery.list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Goal> findOverdue(Person person, MeasureType measureType) {
        Query namedQuery = getSession().getNamedQuery(Goal.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_OVERDUE);
        namedQuery.setParameter("person", person);
        namedQuery.setParameter("measureType", measureType);
        namedQuery.setParameter("currentTime", Calendar.getInstance().getTime());
        return namedQuery.list();
    }

    @Override
    @Transactional
    public Goal save(Goal goal) {
        validate(goal);
        if (measureTypeDao.findByTypeAndUnit(goal.getMeasureType()) == null) {
            measureTypeDao.save(goal.getMeasureType());
        }
        getSession().save(goal);
        return goal;
    }

    @Autowired
    public void setMeasureTypeDao(MeasureTypeDao measureTypeDao) {
        this.measureTypeDao = measureTypeDao;
    }
}