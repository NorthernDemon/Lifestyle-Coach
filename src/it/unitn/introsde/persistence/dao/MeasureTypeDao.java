package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.MeasureType;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MeasureTypeDao extends AbstractDao<MeasureType> {

    @Transactional(readOnly = true)
    public MeasureType findByTypeAndUnit(String type, String unit) {
        Query namedQuery = getSession().getNamedQuery(MeasureType.FIND_BY_TYPE_AND_UNIT);
        namedQuery.setParameter("type", type);
        namedQuery.setParameter("unit", unit);
        return (MeasureType) namedQuery.uniqueResult();
    }
}