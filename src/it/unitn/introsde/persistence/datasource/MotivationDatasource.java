package it.unitn.introsde.persistence.datasource;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MotivationDatasource {

    public static void main(String args[]) throws Exception {
        System.out.println("Cat: " + getCat());
        System.out.println("Joke: " + getJoke("Victor", "Ekimov"));
    }

    /**
     * Returns a random cat picture
     *
     * @return img link
     * @throws UnirestException
     */
    public static String getCat() throws UnirestException {
        return Unirest.get("https://nijikokun-random-cats.p.mashape.com/random")
                .header("X-Mashape-Key", "B57UnpnPUsmsh2D8N0huZESZ4ne7p1W9vZIjsnaEdJ5JbY7Ndf")
                .header("Accept", "application/json")
                .asJson().getBody().getObject().getString("source");
    }

    /**
     * Returns a random joke about given person
     *
     * @param firstName of the person
     * @param lastName  of the person
     * @return joke as string
     * @throws UnirestException
     */
    public static String getJoke(String firstName, String lastName) throws UnirestException {
        return Unirest.get("http://api.icndb.com/jokes/random?firstName=" + firstName + "&lastName=" + lastName)
                .header("Accept", "application/json")
                .asJson().getBody().getObject().getJSONObject("value").getString("joke");
    }
}
