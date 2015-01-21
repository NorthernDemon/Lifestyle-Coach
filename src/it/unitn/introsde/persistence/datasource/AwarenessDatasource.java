package it.unitn.introsde.persistence.datasource;

import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.wrapper.Awareness;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AwarenessDatasource {
    private static final Logger logger = LogManager.getLogger();

    private String[] awareness = {"help slow down bone loss. Weight-bearing activities (like running and jumping jacks) can help keep your bones strong" +
            "Do strengthening activities at least 2 days a week. These include lifting weights or using resistance bands (long rubber strips that stretch)\n"
            , "If you have a health condition, be as active as you can be. Your doctor can help you choose the best activities for your abilities",
            "Too much cholesterol  in your blood can cause a heart attack or a stroke You could have high cholesterol and not know it. " +
                    "The good news is that it's easy to get your cholesterol checked and if your cholesterol is high, you can take steps to control it"};


    public Awareness getAwareness(Person person) {
        return new Awareness(awareness[new Random().nextInt(awareness.length)]);
    }
}
