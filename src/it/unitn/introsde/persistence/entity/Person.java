package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "person", schema = "people")
@JsonRootName("person")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "firstName", "lastName", "birthDate", "healthProfile"})
@NamedQueries({
        @NamedQuery(
                name = Person.FIND_WITHIN_MEASUREMENT_TYPE_WEIGHT,
                query = "SELECT p FROM Person p WHERE p.healthProfile.weight BETWEEN :min AND :max"
        ),
        @NamedQuery(
                name = Person.FIND_WITHIN_MEASUREMENT_TYPE_HEIGHT,
                query = "SELECT p FROM Person p WHERE p.healthProfile.height BETWEEN :min AND :max"
        ),
        @NamedQuery(
                name = Person.FIND_WITHIN_MEASUREMENT_TYPE_STEPS,
                query = "SELECT p FROM Person p WHERE p.healthProfile.steps BETWEEN :min AND :max"
        ),
})
public class Person implements Serializable {

    public static final String FIND_WITHIN_MEASUREMENT_TYPE_WEIGHT = "Person.findWithinMeasurementTypeWeight";
    public static final String FIND_WITHIN_MEASUREMENT_TYPE_HEIGHT = "Person.findWithinMeasurementTypeHeight";
    public static final String FIND_WITHIN_MEASUREMENT_TYPE_STEPS = "Person.findWithinMeasurementTypeSteps";

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @NotNull
    @Column(nullable = false)
    @JsonProperty(value = "firstname", required = true)
    private String firstName;

    @NotNull
    @Column(nullable = false)
    @JsonProperty(value = "lastname", required = true)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonProperty(value = "birthdate", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    @NotNull
    @Embedded
    @JsonProperty(required = true)
    private HealthProfile healthProfile = new HealthProfile();

    public Person() {
    }

    public Person(String firstName, String lastName, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Person(String firstName, String lastName, Date birthDate, HealthProfile healthProfile) {
        this(firstName, lastName, birthDate);
        this.healthProfile = new HealthProfile(healthProfile);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public HealthProfile getHealthProfile() {
        return healthProfile;
    }

    public void setHealthProfile(HealthProfile healthProfile) {
        this.healthProfile = healthProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Person) {
            Person person = (Person) o;

            return Objects.equals(id, person.id)
                    && Objects.equals(firstName, person.firstName)
                    && Objects.equals(lastName, person.lastName)
                    && Objects.equals(birthDate, person.birthDate);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("birthDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(birthDate))
                .add("healthProfile", healthProfile)
                .toString();
    }
}
