package it.unitn.introsde.persistence.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.dao.GoalDao;
import it.unitn.introsde.persistence.datasource.FaceBookDatasource;
import it.unitn.introsde.persistence.entity.Goal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class GoalService {

    private static final Logger logger = LogManager.getLogger();

    private GoalDao goalDao;

    private FaceBookDatasource faceBookDatasource;

    @RequestMapping(value = "/goal", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Goal> createGoal(
            @Valid @RequestBody Goal goal,
            BindingResult result,
            @RequestParam(value = "fbAccessToken") String fbAccessToken) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        goalDao.save(goal);
        faceBookDatasource.postToWall(fbAccessToken, goal);
        logger.debug("Created goal=" + goal);
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @RequestMapping(value = "/goals", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Goal>> getGoals() {
        List<Goal> goals = goalDao.list(Goal.class);
        if (goals.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("goals =" + goals);
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @Autowired
    public void setGoalDao(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    @Autowired
    public void setFaceBookDatasource(FaceBookDatasource faceBookDatasource) {
        this.faceBookDatasource = faceBookDatasource;
    }
}
