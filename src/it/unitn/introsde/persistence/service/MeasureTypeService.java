package it.unitn.introsde.persistence.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.dao.MeasureTypeDao;
import it.unitn.introsde.persistence.entity.MeasureType;
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

/**
 * Created by davie on 1/18/2015.
 */
@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class MeasureTypeService {
    private static final Logger logger = LogManager.getLogger();

    private MeasureTypeDao measureTypeDao;

    @RequestMapping(value = "/measuretype", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<MeasureType> createMeasureType(
            @Valid @RequestBody MeasureType measureType,
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        measureTypeDao.save(measureType);
        logger.debug("Created measureType=" + measureType);
        return new ResponseEntity<>(measureType, HttpStatus.OK);
    }

    @RequestMapping(value = "/measureTypes", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<MeasureType>> getPeople() {
        List<MeasureType> measureTypes = measureTypeDao.list(MeasureType.class);
        if (measureTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("MeasureTypes =" + measureTypes);
        return new ResponseEntity<>(measureTypes, HttpStatus.OK);
    }

    @RequestMapping(value = "/getmeasureTypeById/{measureTypeId}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<MeasureType> getMeasureTypeByID(@PathVariable("measureTypeId") int measureTypeId) {
        MeasureType measureType = measureTypeDao.get(MeasureType.class, measureTypeId);
        if (measureType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("MeasureType =" + measureType);
        return new ResponseEntity<>(measureType, HttpStatus.OK);
    }


    @Autowired
    public void setMeasureTypeDao(MeasureTypeDao measureTypeDao) {
        this.measureTypeDao = measureTypeDao;
    }
}
