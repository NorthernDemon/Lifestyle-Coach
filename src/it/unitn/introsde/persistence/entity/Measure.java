package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "measure", schema = "people")
@JsonRootName("measure")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "created", "type", "value", "units"})
@NamedQueries({
        @NamedQuery(
                name = Measure.FIND_CURRENT_MEASURE_BY_PERSON,
                query = "SELECT m FROM Measure m WHERE m.person = :person AND m.created =" +
                        " (SELECT MAX(mm.created) FROM Measure mm WHERE mm.person = :person AND mm.type = m.type)"
        ),
        @NamedQuery(
                name = Measure.FIND_HISTORY_MEASURE_BY_PERSON_AND_MEASURE_TYPE,
                query = "SELECT m FROM Measure m WHERE m.person = :person AND m.type = :type AND m.created !=" +
                        " (SELECT MAX(mm.created) FROM Measure mm WHERE mm.person = :person AND mm.type = m.type)" +
                        " ORDER BY m.created"
        ),
        @NamedQuery(
                name = Measure.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_ID,
                query = "SELECT m FROM Measure m WHERE m.person = :person AND m.type = :type AND m.id = :id"
        ),
        @NamedQuery(
                name = Measure.FIND_MEASURE_TYPES,
                query = "SELECT DISTINCT m.type FROM Measure m"
        ),
        @NamedQuery(
                name = Measure.FIND_BY_PERSON_AND_MEASURE_TYPE_WITHIN_DATES,
                query = "SELECT m FROM Measure m WHERE" +
                        " m.person = :person AND m.type = :type AND m.created BETWEEN :after AND :before" +
                        " ORDER BY m.created"
        ),
        @NamedQuery(
                name = Measure.FIND_PEOPLE_BY_MEASURE_TYPE_WITHIN_RANGE,
                query = "SELECT DISTINCT m.person FROM Measure m WHERE m.type = :type AND m.value BETWEEN :min AND :max"
        ),
        @NamedQuery(
                name = Measure.DELETE_BY_PERSON,
                query = "DELETE FROM Measure m WHERE m.person = :person"
        ),
})
public class Measure implements Serializable {

    public static final String FIND_CURRENT_MEASURE_BY_PERSON = "Measure.findCurrentMeasureByPerson";
    public static final String FIND_HISTORY_MEASURE_BY_PERSON_AND_MEASURE_TYPE = "Measure.findHistoryMeasureByPersonAndMeasureType";
    public static final String FIND_BY_PERSON_AND_MEASURE_TYPE_AND_ID = "Measure.findByPersonAndMeasureTypeAndId";
    public static final String FIND_MEASURE_TYPES = "Measure.findMeasureTypes";
    public static final String FIND_BY_PERSON_AND_MEASURE_TYPE_WITHIN_DATES = "Measure.findByPersonAndMeasureTypeWithinDates";
    public static final String FIND_PEOPLE_BY_MEASURE_TYPE_WITHIN_RANGE = "Measure.findPeopleByMeasureTypeWithinRange";
    public static final String DELETE_BY_PERSON = "Measure.deleteByPerson";

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "personId", nullable = false)
    @JsonIgnore
    private Person person;

    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date created = Calendar.getInstance().getTime();

    @NotNull
    @Column(nullable = false)
    @JsonProperty(required = true)
    private String type;

    @NotNull
    @Column(nullable = false)
    @JsonProperty(required = true)
    private String value;

    @NotNull
    @Column(nullable = false)
    @JsonProperty(required = true)
    private String units; // string, integer, real

    public Measure() {
    }

    public Measure(Person person, String type, String value, String units) {
        this.person = person;
        this.type = type;
        this.value = value;
        this.units = units;
    }

    public Measure(Person person, Date created, String type, String value, String units) {
        this(person, type, value, units);
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Measure) {
            Measure measure = (Measure) o;

            return Objects.equals(id, measure.id)
                    && Objects.equals(created, measure.created)
                    && Objects.equals(type, measure.type)
                    && Objects.equals(value, measure.value);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, type, value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("person", person == null ? "null" : person.getId())
                .add("created", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(created))
                .add("type", type)
                .add("value", value)
                .add("units", units)
                .toString();
    }
}