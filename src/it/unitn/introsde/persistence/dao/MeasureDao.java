package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.Goal;
import it.unitn.introsde.persistence.entity.Measure;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class MeasureDao extends AbstractDao<Measure> {

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Measure> findMeasuresSinceGoalStart(Goal goal) {
        Query namedQuery = getSession().getNamedQuery(Measure.FIND_BY_PERSON_AND_MEASURE_TYPE_SINCE_DATE);
        namedQuery.setParameter("person", goal.getPerson());
        namedQuery.setParameter("measureType", goal.getMeasureType());
        namedQuery.setParameter("start", goal.getStart());
        return namedQuery.list();
    }

    @Transactional(readOnly = true)
    public Measure findLatestMeasureForGoal(Goal goal) {
        Query namedQuery = getSession().getNamedQuery(Measure.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_LATEST_DATE);
        namedQuery.setParameter("person", goal.getPerson());
        namedQuery.setParameter("measureType", goal.getMeasureType());
        return (Measure) namedQuery.uniqueResult();
    }
}