package it.unitn.introsde.persistence.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Convenient class to abstract:
 * - hibernate session factory
 * - CRUD operations
 * - JSR-303 Bean Validation
 *
 * @param <T> entity class
 */
@Repository
public abstract class AbstractDao<T> {

    private SessionFactory sessionFactory;

    private ValidatorFactory validatorFactory;

    @Transactional
    public T save(T entity) {
        validate(entity);
        getSession().save(entity);
        return entity;
    }

    @Transactional
    public void delete(T entity) {
        validate(entity);
        getSession().delete(entity);
    }

    @Transactional
    public T update(T entity) {
        validate(entity);
        getSession().update(entity);
        return entity;
    }

    @Transactional
    public T get(Class<T> clazz, int id) {
        return clazz.cast(getSession().get(clazz, id));
    }

    @Transactional
    public T load(Class<T> clazz, int id) {
        return clazz.cast(getSession().load(clazz, id));
    }

    @Transactional
    public List<T> getAll(String queryString) {
        Query query = getSession().createQuery(queryString);
        List<T> list = query.list();
        return list;
    }

    /**
     * Performs bean validation with JSR-303
     *
     * @param entity bean to validate
     * @throws ConstraintViolationException if bean validation fails
     */
    protected void validate(T entity) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> constraintViolations = validatorFactory.getValidator().validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException("Invalid object of class=" + entity.getClass(), new HashSet<ConstraintViolation<?>>(constraintViolations));
        }
    }

    /**
     * Returns current hibernate session of session factory
     *
     * @return current session
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    public void setValidatorFactory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }
}
