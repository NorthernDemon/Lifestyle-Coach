package it.unitn.introsde.persistence.service;

import it.unitn.introsde.BeanValidationException;
import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.dao.GoalDao;
import it.unitn.introsde.persistence.entity.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class GoalService {

    private GoalDao goalDao;

    @RequestMapping(value = "/goal", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Goal> createProfile(@Valid @RequestBody Goal goal, BindingResult result) {
        if (result.hasErrors()) {
            throw new BeanValidationException(result.getAllErrors());
        }
        goalDao.save(goal);
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @Autowired
    public void setGoalDao(GoalDao goalDao) {
        this.goalDao = goalDao;
    }
}
