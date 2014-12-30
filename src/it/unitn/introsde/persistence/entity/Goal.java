package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.ServiceConfiguration;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "goal", schema = ServiceConfiguration.SCHEMA)
@JsonRootName("goal")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "creator", "person", "measureType", "value", "message", "start", "end", "finished"})
@NamedQueries({
        @NamedQuery(
                name = Goal.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_ACCOMPLISHED,
                query = "SELECT g FROM Goal g WHERE g.person = :person AND g.measureType = :measureType" +
                        " AND g.finished IS NOT NULL" +
                        " ORDER BY g.finished DESC"
        ),
        @NamedQuery(
                name = Goal.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_CURRENT,
                query = "SELECT g FROM Goal g WHERE g.person = :person AND g.measureType = :measureType" +
                        " AND g.finished IS NULL AND g.end >= :currentTime" +
                        " ORDER BY g.start"
        ),
        @NamedQuery(
                name = Goal.FIND_BY_PERSON_AND_MEASURE_TYPE_AND_OVERDUE,
                query = "SELECT g FROM Goal g WHERE g.person = :person AND g.measureType = :measureType" +
                        " AND g.finished IS NULL AND g.end < :currentTime" +
                        " ORDER BY g.start"
        ),
})
public class Goal implements Serializable {

    public static final String FIND_BY_PERSON_AND_MEASURE_TYPE_AND_ACCOMPLISHED = "Goal.findByPersonAndMeasureTypeAndAccomplished";
    public static final String FIND_BY_PERSON_AND_MEASURE_TYPE_AND_CURRENT = "Goal.findByPersonAndMeasureTypeAndCurrent";
    public static final String FIND_BY_PERSON_AND_MEASURE_TYPE_AND_OVERDUE = "Goal.findByPersonAndMeasureTypeAndOverdue";

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @Valid
    @NotNull
    @ManyToOne
    @JoinColumn(name = "creatorId", nullable = false, updatable = false)
    @JsonProperty(required = true)
    private Person creator;

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
    @NotEmpty
    @Size(max = 255)
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private String message;

    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date start;

    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date end;

    @Nullable
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date finished;

    public Goal() {
    }

    public Goal(Person creator, Person person, MeasureType measureType, double value, String message, Date start, Date end) {
        this.creator = creator;
        this.person = person;
        this.measureType = measureType;
        this.value = value;
        this.message = message;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Goal) {
            Goal object = (Goal) o;

            return Objects.equals(id, object.id)
                    && Objects.equals(creator, object.creator)
                    && Objects.equals(person, object.person)
                    && Objects.equals(start, object.start);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, person, start);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("creator", creator == null ? "null" : creator)
                .add("person", person == null ? "null" : person)
                .add("measureType", measureType == null ? "null" : measureType)
                .add("value", value)
                .add("message", message)
                .add("start", start == null ? "null" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(start))
                .add("end", end == null ? "null" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(end))
                .add("finished", finished == null ? "null" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(finished))
                .toString();
    }
}