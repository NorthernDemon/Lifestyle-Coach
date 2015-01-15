package it.unitn.introsde.persistence.datasource;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import it.unitn.introsde.helpers.Motivation;
import it.unitn.introsde.persistence.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MotivationDatasource {

    private static final Logger logger = LogManager.getLogger();

    public Motivation getMotivated(Person person) {
        try {
            if (new Random().nextBoolean()) {
                return new Motivation("image", getCat());
            } else {
                return new Motivation("text", getJoke(person.getName(), person.getSurname()));
            }
        } catch (UnirestException e) {
            logger.error("Unirest failed in motivation datasource", e);
            return null;
        }
    }

    /**
     * Returns a random cat picture
     *
     * @return img link
     * @throws UnirestException
     */
    private String getCat() throws UnirestException {
        return Unirest.get("https://nijikokun-random-cats.p.mashape.com/random")
                .header("X-Mashape-Key", "B57UnpnPUsmsh2D8N0huZESZ4ne7p1W9vZIjsnaEdJ5JbY7Ndf")
                .header("Accept", "application/json")
//                .asJson().getBody().getObject().getString("source");
                .asString().getBody(); // for testing purpose: service fails due to 502 ATM
    }

    /**
     * Returns a random joke about given person
     *
     * @param firstName of the person
     * @param lastName  of the person
     * @return joke as string
     * @throws UnirestException
     */
    private String getJoke(String firstName, String lastName) throws UnirestException {
        return Unirest.get("http://api.icndb.com/jokes/random?firstName=" + firstName + "&lastName=" + lastName)
                .header("Accept", "application/json")
                .asJson().getBody().getObject().getJSONObject("value").getString("joke");
    }
}
