package it.unitn.introsde.util;

import it.unitn.introsde.persistence.entity.Measure;
import it.unitn.introsde.persistence.entity.MeasureType;
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

    private static int id;

    private static String[] girlName = ("Sophia,Emma,Olivia,Isabella,Mia,Ava,Lily,Zoe,Emily,Chloe,Layla,Madison,Madelyn,Abigail," +
            "Aubrey,Charlotte,Amelia,Ella,Kaylee,Avery,Aaliyah,Hailey,Hannah,Addison,Riley,Harper,Aria,Arianna," +
            "Mackenzie,Lila,Evelyn,Adalyn,Grace,Brooklyn,Ellie,Anna,Kaitlyn,Isabelle,Sophie,Scarlett,Natalie,Leah," +
            "Sarah,Nora,Mila,Elizabeth,Lillian,Kylie,Audrey,Lucy,Maya,Annabelle,Makayla,Gabriella,Elena,Victoria," +
            "Claire,Savannah,Peyton,Maria,Alaina,Kennedy,Stella,Liliana,Allison,Samantha,Keira,Alyssa,Reagan,Molly," +
            "Alexandra,Violet,Charlie,Julia,Sadie,Ruby,Eva,Alice,Eliana,Taylor,Callie,Penelope,Camilla,Bailey,Kaelyn," +
            "Alexis,Kayla,Katherine,Sydney,Lauren,Jasmine,London,Bella,Adeline,Caroline,Vivian,Juliana,Gianna," +
            "Skyler,Jordyn").split(",");

    /**
     * Generate random measure
     *
     * @param person owner of the measure
     * @return random measure
     */
    public static Measure getMeasure(Person person) {
        return new Measure(person, getMeasureType(), getDouble(100), getDate(1950));
    }

    /**
     * Generate random measure Type
     *
     * @return random measureType
     */
    private static MeasureType getMeasureType() {
        if (new Random().nextBoolean()) {
            return new MeasureType("weight", "kilograms");
        } else {
            return new MeasureType("height", "meters");
        }
    }

    /**
     * Wrapper for returning random Integer
     *
     * @param n the bound on the random number to be returned.  Must be
     *          positive
     * @return the next pseudorandom, uniformly distributed {@code int}
     * value between {@code 0} (inclusive) and {@code n} (exclusive)
     * from this random number generator's sequence
     */
    private static int getInteger(int n) {
        return new Random().nextInt(n);
    }

    /**
     * Wrapper for returning random Double with 1 decimal after dot
     *
     * @param n the bound on the random number to be returned.  Must be
     *          positive
     * @return the next pseudorandom, uniformly distributed {@code double}
     * value between {@code 0.0} (inclusive) and {@code n} (exclusive)
     * from this random number generator's sequence
     */
    private static double getDouble(int n) {
        return Math.round(new Random().nextDouble() * n) / 10.0;
    }

    /**
     * Generates person with random first and last names, measure, id and birthDate starting from 1950 year
     *
     * @return random person
     */
    public static Person getPerson() {
        return new Person(getName(), getName(), getDate(1950), id++, id++);
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
