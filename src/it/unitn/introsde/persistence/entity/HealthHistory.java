package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "health_history", schema = "people")
@JsonRootName(value = "measure")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "person", "measurementType", "value", "created"})
@NamedQueries({
        @NamedQuery(
                name = HealthHistory.FIND_BY_PERSON_AND_MEASUREMENT_TYPE,
                query = "SELECT hh FROM HealthHistory hh WHERE hh.person = :person AND hh.measurementType = :measurementType"
        ),
        @NamedQuery(
                name = HealthHistory.FIND_BY_PERSON_AND_MEASUREMENT_TYPE_AND_ID,
                query = "SELECT hh FROM HealthHistory hh WHERE hh.person = :person AND hh.measurementType = :measurementType AND hh.id = :id"
        ),
        @NamedQuery(
                name = HealthHistory.FIND_BY_PERSON_AND_MEASUREMENT_TYPE_WITHIN_DATES,
                query = "SELECT hh FROM HealthHistory hh WHERE hh.person = :person AND hh.measurementType = :measurementType AND hh.created BETWEEN :afterDate AND :beforeDate"
        ),
})
public class HealthHistory implements Serializable {

    public static final String FIND_BY_PERSON_AND_MEASUREMENT_TYPE = "HealthHistory.findByPersonAndMeasurementType";
    public static final String FIND_BY_PERSON_AND_MEASUREMENT_TYPE_AND_ID = "HealthHistory.findByPersonAndMeasurementTypeAndId";
    public static final String FIND_BY_PERSON_AND_MEASUREMENT_TYPE_WITHIN_DATES = "HealthHistory.findByPersonAndMeasurementTypeWithinDates";

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    @JacksonXmlProperty(localName = "mid")
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "personId")
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
    @JsonIgnore
    private String measurementType;

    @NotNull
    @Column(nullable = false)
    @JsonProperty(required = true)
    private double value;

    public HealthHistory() {
    }

    public HealthHistory(Person person, String measurementType, double value) {
        this.person = person;
        this.measurementType = measurementType;
        this.value = value;
    }

    public HealthHistory(Person person, String measurementType, double value, Date created) {
        this(person, measurementType, value);
        this.created = created;
    }

    public HealthHistory(HealthHistory healthHistory) {
        this.person = healthHistory.person;
        this.created = healthHistory.created;
        this.measurementType = healthHistory.measurementType;
        this.value = healthHistory.value;
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

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
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

        if (o instanceof HealthHistory) {
            HealthHistory healthHistory = (HealthHistory) o;

            return Objects.equals(id, healthHistory.id)
                    && Objects.equals(person, healthHistory.person)
                    && Objects.equals(created, healthHistory.created)
                    && Objects.equals(measurementType, healthHistory.measurementType)
                    && Objects.equals(value, healthHistory.value);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, created, measurementType, value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("person", person)
                .add("created", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(created))
                .add("measurementType", measurementType)
                .add("value", value)
                .toString();
    }
}