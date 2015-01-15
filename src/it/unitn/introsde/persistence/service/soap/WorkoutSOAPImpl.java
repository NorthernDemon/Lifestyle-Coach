package it.unitn.introsde.persistence.service.soap;

import it.unitn.introsde.helpers.Workout;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.datasource.WorkoutDatasource;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.jws.WebMethod;
import javax.jws.WebService;

@Service
@WebService(endpointInterface = "it.unitn.introsde.persistence.service.soap.WorkoutSOAP")
public class WorkoutSOAPImpl implements WorkoutSOAP {

    private static final Logger logger = LogManager.getLogger();

    private PersonDao personDao;

    private WorkoutDatasource workoutDatasource;

    @Override
    public Workout getWorkout(@PathVariable("personId") int personId) {
        Person person = personDao.get(Person.class, personId);
        if (person == null) {
            throw new IllegalArgumentException("Person not found");
        }
        Workout workout = workoutDatasource.getWorkout();
        if (workout == null) {
            throw new IllegalArgumentException("Workout not found");
        }
        logger.debug("Person workouted with=" + workout);
        return workout;
    }

    @Autowired
    @WebMethod(exclude = true)
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Autowired
    @WebMethod(exclude = true)
    public void setWorkoutDatasource(WorkoutDatasource workoutDatasource) {
        this.workoutDatasource = workoutDatasource;
    }
}
