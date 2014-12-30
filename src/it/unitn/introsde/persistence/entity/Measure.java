package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.ServiceConfiguration;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "measure", schema = ServiceConfiguration.SCHEMA)
@JsonRootName("measure")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "person", "measureType", "value", "created"})
@NamedQueries({
        @NamedQuery(
                name = Measure.FIND_BY_PERSON_AND_MEASURE_TYPE_SINCE_DATE,
                query = "SELECT m FROM Measure m WHERE m.person = :person AND m.measureType = :measureType AND m.created >= :start" +
                        " ORDER BY m.created DESC"
        ),
        @NamedQuery(
                name = Measure.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_LATEST_DATE,
                query = "SELECT m FROM Measure m WHERE m.created =" +
                        " (SELECT MAX(mm.created) FROM Measure mm WHERE mm.person = :person AND mm.measureType = :measureType)"
        ),
})
public class Measure implements Serializable {

    public static final String FIND_BY_PERSON_AND_MEASURE_TYPE_SINCE_DATE = "Measure.findByPersonAndMeasureTypeSinceDate";
    public static final String FIND_BY_PERSON_AND_MEASURE_TYPE_AND_LATEST_DATE = "Measure.findByPersonAndMeasureTypeAndLatestDate";

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @Valid
    @NotNull
    @ManyToOne
    @JoinColumn(name = "personId", nullable = false, updatable = false)
    @JsonProperty(required = true)
    private Person person;

    @Valid
    @NotNull
    @ManyToOne
    @JoinColumn(name = "measureTypeId", nullable = false, updatable = false)
    @JsonProperty(required = true)
    private MeasureType measureType;

    @Range
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private double value;

    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date created = Calendar.getInstance().getTime();

    public Measure() {
    }

    public Measure(Person person, MeasureType measureType, double value) {
        this.person = person;
        this.measureType = measureType;
        this.value = value;
    }

    public Measure(Person person, MeasureType measureType, double value, @Past Date created) {
        this(person, measureType, value);
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

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Measure) {
            Measure object = (Measure) o;

            return Objects.equals(id, object.id)
                    && Objects.equals(person, object.person)
                    && Objects.equals(value, object.value)
                    && Objects.equals(created, object.created);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, value, created);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("person", person == null ? "null" : person)
                .add("measureType", measureType == null ? "null" : measureType)
                .add("value", value)
                .add("created", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(created))
                .toString();
    }
}