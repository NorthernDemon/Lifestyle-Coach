package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.MeasureType;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;

@Repository
public class MeasureTypeDao extends AbstractDao<MeasureType> {

    @Nullable
    @Transactional(readOnly = true)
    public MeasureType findByTypeAndUnit(MeasureType measureType) {
        Query namedQuery = getSession().getNamedQuery(MeasureType.FIND_BY_TYPE_AND_UNIT);
        namedQuery.setParameter("type", measureType.getType());
        namedQuery.setParameter("unit", measureType.getUnit());
        return (MeasureType) namedQuery.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<MeasureType> list() {
        return (List<MeasureType>) getSession().createCriteria(MeasureType.class).list();
    }
}