package it.unitn.introsde.persistence;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.dao.MeasureDao;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.MeasureTypes;
import it.unitn.introsde.wrapper.Measures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class MeasureService {

    private PersonDao personDao;

    private MeasureDao measureDao;

    /**
     * Request #6: GET /person/{id}/{measureType} should return the list of values (the history) of {measureType} (e.g. weight) for person identified by {id}
     * <p/>
     * Extra #3 (Request #11): GET /person/{id}/{measureType}?before={beforeDate}&after={afterDate} should return the history of {measureType} (e.g., weight) for person {id} in the specified range of date
     */
    @RequestMapping(value = {"/person/{id}/{measureType}", "/person/{id}/{measureType}?before={beforeDate}&after={afterDate}"}, method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPersonMeasure(@PathVariable("id") int id,
                                              @PathVariable("measureType") String measureType,
                                              @RequestParam(value = "before", required = false) String before,
                                              @RequestParam(value = "after", required = false) String after) throws ParseException {
        List<Measure> measures;
        if (after != null && before != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            measures = measureDao.findByPersonAndMeasurementTypeWithinDates(personDao.load(Person.class, id), measureType, dateFormat.parse(after), dateFormat.parse(before));
        } else {
            measures = measureDao.findHistoryMeasureByPersonAndMeasureType(personDao.load(Person.class, id), measureType);
        }
        return new ResponseEntity<>(new Measures(measures), HttpStatus.OK);
    }

    /**
     * Request #7: *GET /person/{id}/{measureType}/{mid} should return the value of {measureType} (e.g. weight) identified by {mid} for person identified by {id}
     */
    @RequestMapping(value = "/person/{id}/{measureType}/{mid}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public Measure getMeasure(@PathVariable("id") int id,
                              @PathVariable("measureType") String measureType,
                              @PathVariable("mid") int measureId) {
        return measureDao.findByPersonAndMeasureTypeAndId(personDao.load(Person.class, id), measureType, measureId);
    }

    /**
     * Request #8: POST /person/{id}/{measureType} should save a new value for the {measureType} (e.g. weight) of person identified by {id} and archive the old value in the history
     */
    @RequestMapping(value = "/person/{id}/{measureType}", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public Measure saveMeasure(@PathVariable("id") int id,
                               @RequestBody Measure measure) {
        measure.setPerson(personDao.get(Person.class, id));
        return measureDao.save(measure);
    }

    /**
     * Request #9: GET /measureTypes should return the list of measures your model supports in the following formats
     */
    @RequestMapping(value = "/measureTypes", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public MeasureTypes getMeasureTypes() {
        return new MeasureTypes(measureDao.findMeasureTypes());
    }

    /**
     * Extra #2 (Request #10): PUT /person/{id}/{measureType}/{mid} should update the value for the {measureType} (e.g., weight) identified by {mid}, related to the person identified by {id}
     */
    @RequestMapping(value = "/person/{id}/{measureType}/{mid}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public Measure updateMeasure(@PathVariable("id") int id,
                                 @RequestBody Measure measure) {
        measure.setPerson(personDao.get(Person.class, id));
        return measureDao.update(measure);
    }

    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Autowired
    public void setMeasureDao(MeasureDao measureDao) {
        this.measureDao = measureDao;
    }
}
