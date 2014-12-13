package it.unitn.introsde.persistence;

import it.unitn.introsde.StandaloneClientLauncher;
import it.unitn.introsde.persistence.dao.HealthHistoryDao;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.HealthHistory;
import it.unitn.introsde.persistence.entity.HealthProfile;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.MeasurementHistory;
import it.unitn.introsde.wrapper.MeasurementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = StandaloneClientLauncher.SERVER_STUDENT_NAME)
public class MeasurementService {

    private PersonDao personDao;

    private HealthHistoryDao healthHistoryDao;

    /**
     * Request #6: GET /person/{id}/{measureType} should return the list of values (the history) of {measureType} (e.g. weight) for person identified by {id}
     * <p/>
     * Extra #3 (Request #11): GET /person/{id}/{measureType}?before={beforeDate}&after={afterDate} should return the history of {measureType} (e.g., weight) for person {id} in the specified range of date
     */
    @RequestMapping(value = {"/person/{id}/{measureType}", "/person/{id}/{measureType}?before={beforeDate}&after={afterDate}"}, method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPersonMeasurement(@RequestHeader(value = "Accept") String accept,
                                                  @PathVariable("id") int id,
                                                  @PathVariable("measureType") String measurementType,
                                                  @RequestParam(value = "before", required = false) String beforeDate,
                                                  @RequestParam(value = "after", required = false) String afterDate) throws ParseException {
        List<HealthHistory> healthHistories;
        if (afterDate != null && beforeDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            healthHistories = healthHistoryDao.findByPersonAndMeasurementTypeWithinDates(personDao.load(Person.class, id), measurementType, dateFormat.parse(afterDate), dateFormat.parse(beforeDate));
        } else {
            healthHistories = healthHistoryDao.findByPersonAndMeasurementType(personDao.load(Person.class, id), measurementType);
        }
        switch (accept.toLowerCase()) {
            case MediaType.APPLICATION_XML_VALUE:
                return new ResponseEntity<>(new MeasurementHistory(healthHistories), HttpStatus.OK);
            case MediaType.APPLICATION_JSON_VALUE:
                if (StandaloneClientLauncher.JSON_UNWRAP_LIST) {
                    return new ResponseEntity<>(healthHistories, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MeasurementHistory(healthHistories), HttpStatus.OK);
                }
            default:
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Request #7: *GET /person/{id}/{measureType}/{mid} should return the value of {measureType} (e.g. weight) identified by {mid} for person identified by {id}
     */
    @RequestMapping(value = "/person/{id}/{measureType}/{mid}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public HealthHistory getMeasurement(@PathVariable("id") int id,
                                        @PathVariable("measureType") String measurementType,
                                        @PathVariable("mid") int measurementId) {
        return healthHistoryDao.findByPersonAndMeasurementTypeAndId(personDao.load(Person.class, id), measurementType, measurementId);
    }

    /**
     * Request #8: POST /person/{id}/{measureType} should save a new value for the {measureType} (e.g. weight) of person identified by {id} and archive the old value in the history
     */
    @RequestMapping(value = "/person/{id}/{measureType}", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public HealthHistory postHealthProfile(@PathVariable("id") int id,
                                           @PathVariable("measureType") String measurementType,
                                           @RequestBody HealthHistory healthHistory) {
        Person person = personDao.get(Person.class, id);
        HealthProfile healthProfile = new HealthProfile(person.getHealthProfile());
        HealthHistory savedHealthHistory = null;
        switch (measurementType) {
            case "height":
                savedHealthHistory = healthHistoryDao.save(new HealthHistory(person, measurementType, healthProfile.getHeight()));
                person.getHealthProfile().setHeight(healthHistory.getValue());
                break;
            case "weight":
                savedHealthHistory = healthHistoryDao.save(new HealthHistory(person, measurementType, healthProfile.getWeight()));
                person.getHealthProfile().setWeight(healthHistory.getValue());
                break;
            case "steps":
                savedHealthHistory = healthHistoryDao.save(new HealthHistory(person, measurementType, healthProfile.getSteps()));
                person.getHealthProfile().setSteps(healthHistory.getValue());
                break;
        }
        personDao.update(person);
        return savedHealthHistory;
    }

    /**
     * Request #9: GET /measureTypes should return the list of measures your model supports in the following formats
     */
    @RequestMapping(value = "/measureTypes", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getMeasurementTypes(@RequestHeader(value = "Accept") String accept) {
        Field[] fields = HealthProfile.class.getDeclaredFields();
        List<String> measures = new ArrayList<>(fields.length);
        for (Field field : fields) {
            measures.add(field.getName());
        }
        MeasurementType measurementType = new MeasurementType(measures);
        switch (accept.toLowerCase()) {
            case MediaType.APPLICATION_XML_VALUE:
                return new ResponseEntity<>(measurementType, HttpStatus.OK);
            case MediaType.APPLICATION_JSON_VALUE:
                if (StandaloneClientLauncher.JSON_UNWRAP_LIST) {
                    return new ResponseEntity<>(measurementType.getMeasurementTypes(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(measurementType, HttpStatus.OK);
                }
            default:
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Extra #2 (Request #10): PUT /person/{id}/{measureType}/{mid} should update the value for the {measureType} (e.g., weight) identified by {mid}, related to the person identified by {id}
     */
    @RequestMapping(value = "/person/{id}/{measureType}/{mid}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public HealthHistory putMeasurement(@PathVariable("id") int id,
                                        @PathVariable("measureType") String measurementType,
                                        @PathVariable("mid") int measurementId,
                                        @RequestBody HealthHistory healthHistory) {
        HealthHistory existingHealthHistory = healthHistoryDao.findByPersonAndMeasurementTypeAndId(personDao.load(Person.class, id), measurementType, measurementId);
        existingHealthHistory.setValue(healthHistory.getValue());
        existingHealthHistory.setCreated(healthHistory.getCreated());
        return healthHistoryDao.update(existingHealthHistory);
    }

    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Autowired
    public void setHealthHistoryDao(HealthHistoryDao healthHistoryDao) {
        this.healthHistoryDao = healthHistoryDao;
    }
}
