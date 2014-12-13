package it.unitn.introsde.util;

import it.unitn.introsde.persistence.entity.HealthHistory;
import it.unitn.introsde.persistence.entity.HealthProfile;
import it.unitn.introsde.persistence.entity.Person;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Convenient class for providing fake data input
 * <p/>
 * WARNING: it can produce really fatty and tall girls!
 */
public abstract class RandomUtil {

    private static String[] girlName = ("Sophia,Emma,Olivia,Isabella,Mia,Ava,Lily,Zoe,Emily,Chloe,Layla,Madison,Madelyn,Abigail," +
            "Aubrey,Charlotte,Amelia,Ella,Kaylee,Avery,Aaliyah,Hailey,Hannah,Addison,Riley,Harper,Aria,Arianna," +
            "Mackenzie,Lila,Evelyn,Adalyn,Grace,Brooklyn,Ellie,Anna,Kaitlyn,Isabelle,Sophie,Scarlett,Natalie,Leah," +
            "Sarah,Nora,Mila,Elizabeth,Lillian,Kylie,Audrey,Lucy,Maya,Annabelle,Makayla,Gabriella,Elena,Victoria," +
            "Claire,Savannah,Peyton,Maria,Alaina,Kennedy,Stella,Liliana,Allison,Samantha,Keira,Alyssa,Reagan,Molly," +
            "Alexandra,Violet,Charlie,Julia,Sadie,Ruby,Eva,Alice,Eliana,Taylor,Callie,Penelope,Camilla,Bailey,Kaelyn," +
            "Alexis,Kayla,Katherine,Sydney,Lauren,Jasmine,London,Bella,Adeline,Caroline,Vivian,Juliana,Gianna," +
            "Skyler,Jordyn").split(",");

    /**
     * Generate random health history
     *
     * @param person owner of the health history
     * @return random health history
     */
    public static HealthHistory getHealthHistory(Person person) {
        int value = new Random().nextInt(3);
        if (value == 0) {
            return new HealthHistory(person, "height", getDouble(100), getDate(2010));
        } else if (value == 1) {
            return new HealthHistory(person, "weight", getDouble(1000), getDate(2010));
        } else {
            return new HealthHistory(person, "steps", getDouble(10000), getDate(2010));
        }
    }

    /**
     * Generates person with random first and last names, health profile, id and birthDate starting from 1950 year
     *
     * @return random person
     */
    public static Person getPerson() {
        return new Person(
                getName(),
                getName(),
                getDate(1950),
                getHealthProfile());
    }

    /**
     * Generates health profile with random values
     *
     * @return random health profile
     */
    private static HealthProfile getHealthProfile() {
        return new HealthProfile(getDouble(100), getDouble(1000), getDouble(10000));
    }

    private static double getDouble(int value) {
        return Math.round(new Random().nextDouble() * value) / 10.0;
    }

    /**
     * Generates random date from given year until current time
     *
     * @param fromYear lower year limit
     * @return random date between fromYear and current time
     */
    private static Date getDate(int fromYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, fromYear + new Random().nextInt(Calendar.getInstance().get(Calendar.YEAR) - fromYear));
        calendar.set(Calendar.MONTH, new Random().nextInt(11));
        calendar.set(Calendar.DAY_OF_MONTH, 1 + new Random().nextInt(31));
        return calendar.getTime();
    }

    /**
     * Generates random name
     *
     * @return random name
     */
    private static String getName() {
        return girlName[new Random().nextInt(girlName.length)];
    }
}
