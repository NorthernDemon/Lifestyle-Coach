package it.unitn.introsde.persistence.service.soap;

import it.unitn.introsde.helpers.Workout;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface WorkoutSOAP {

    @WebMethod
    @WebResult(name = "workout")
    Workout getWorkout(@WebParam(name = "personId") int personId);
}
