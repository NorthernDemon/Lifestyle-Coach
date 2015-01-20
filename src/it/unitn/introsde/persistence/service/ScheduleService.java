package it.unitn.introsde.persistence.service;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.datasource.GoogleDatasource;
import it.unitn.introsde.wrapper.Schedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by davie on 1/17/2015.
 */
@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class ScheduleService {

    private static final Logger logger = LogManager.getLogger();
    private GoogleDatasource googleDatasource;

    @RequestMapping(value = "/schedule", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Schedule> createSchedule(
            @RequestBody Schedule schedule,
            BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        googleDatasource.createEvent(schedule);
        logger.debug("Created schedule=" + schedule);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @RequestMapping(value = "/calendarEvent/{accesstoken}", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Schedule>> getEvent(@PathVariable("accesstoken") String accesstoken) {
        List<Schedule> schedules = null;
        try {
            schedules = googleDatasource.getCalendarEvent(accesstoken);
            if (schedules.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (GeneralSecurityException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        logger.debug("Schedules =" + schedules);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @Autowired
    public void setGoogleDatasource(GoogleDatasource googleDatasource) {
        this.googleDatasource = googleDatasource;
    }
}
