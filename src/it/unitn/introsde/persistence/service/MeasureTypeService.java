package it.unitn.introsde.persistence.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.dao.MeasureTypeDao;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.datasource.MotivationDatasource;
import it.unitn.introsde.persistence.entity.MeasureType;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.Motivation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by davie on 1/18/2015.
 */
@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class MeasureTypeService {
    private static final Logger logger = LogManager.getLogger();

    private MeasureTypeDao measreTypeDao;

    @RequestMapping(value = "/measuretype", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<MeasureType> createMeasureType(
            @Valid @RequestBody MeasureType measureType,
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        measreTypeDao.save(measureType);
        logger.debug("Created measureType=" + measureType);
        return new ResponseEntity<>(measureType, HttpStatus.OK);
    }

    @Autowired
    public void setMeasreTypeDao(MeasureTypeDao measreTypeDao) {
        this.measreTypeDao = measreTypeDao;
    }
}
