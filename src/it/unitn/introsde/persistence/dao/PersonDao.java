package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PersonDao extends AbstractDao<Person> {

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Person> list() {
        return getSession().createCriteria(Person.class).list();
    }
}