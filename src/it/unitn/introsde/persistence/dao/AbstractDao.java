package it.unitn.introsde.persistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Convenient class to abstract hibernate session factory and CRUD operations
 *
 * @param <T> entity class
 */
@Repository
public abstract class AbstractDao<T> {

    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public T save(T entity) {
        getSession().save(entity);
        return entity;
    }

    @Transactional
    public void delete(T entity) {
        getSession().delete(entity);
    }

    @Transactional
    public T update(T entity) {
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

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
