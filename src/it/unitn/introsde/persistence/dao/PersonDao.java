package it.unitn.introsde.persistence.dao;

import it.unitn.introsde.persistence.entity.Person;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;

@Repository
public class PersonDao extends AbstractDao<Person> {

    @Nullable
    @Transactional(readOnly = true)
    public Person findByFacebookId(int facebookId) {
        Query namedQuery = getSession().getNamedQuery(Person.FIND_BY_FACEBOOK_ID);
        namedQuery.setParameter("facebookId", facebookId);
        return (Person) namedQuery.uniqueResult();
    }

    @Nullable
    @Transactional(readOnly = true)
    public Person findByGoogleId(int googleId) {
        Query namedQuery = getSession().getNamedQuery(Person.FIND_BY_GOOGLE_ID);
        namedQuery.setParameter("googleId", googleId);
        return (Person) namedQuery.uniqueResult();
    }
}