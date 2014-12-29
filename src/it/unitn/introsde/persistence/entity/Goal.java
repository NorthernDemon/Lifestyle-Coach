package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.ServiceConfiguration;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "goal", schema = ServiceConfiguration.SCHEMA)
@JsonRootName("goal")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "creator", "measureType", "value", "message", "start", "end", "finished"})
public class Goal implements Serializable {

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "creatorId", nullable = false, updatable = false)
    @JsonProperty(required = true)
    private Person creator;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "personId", nullable = false, updatable = false)
    @JsonIgnore
    private Person person;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "measureTypeId", nullable = false, updatable = false)
    @JsonProperty(required = true)
    private MeasureType measureType;

    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private double value;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private String message;

    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date start = Calendar.getInstance().getTime();

    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date end = Calendar.getInstance().getTime();

    @Nullable
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date finished = Calendar.getInstance().getTime();

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
            Goal measure = (Goal) o;

            return Objects.equals(id, measure.id)
                    && Objects.equals(creator, measure.creator)
                    && Objects.equals(person, measure.person)
                    && Objects.equals(start, measure.start);
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
                .add("start", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(start))
                .add("end", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(end))
                .add("finished", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(finished))
                .toString();
    }
}