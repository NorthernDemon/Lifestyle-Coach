package it.unitn.introsde.datasource;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import it.unitn.introsde.wrapper.Workout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Workout datasource uses one REST service:
 * - wger.de/api/v2
 */
@Component
public class WorkoutDatasource {

    private static final Logger logger = LogManager.getLogger();

    public Workout getWorkout() {
        try {
            JSONArray result = Unirest.get("http://wger.de/api/v2/exercise/?format=json")
                    .asJson().getBody().getObject().getJSONArray("results");
            JSONObject exercise = result.getJSONObject(new Random().nextInt(result.length()));
            return new Workout(exercise.getString("name"), exercise.getString("description"));
        } catch (UnirestException e) {
            logger.error("Unirest failed", e);
            return null;
        }
    }
}
