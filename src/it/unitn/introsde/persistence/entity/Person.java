package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "person", schema = "lifestylecoach")
@JsonRootName("person")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "firstName", "lastName", "birthday", "currentMeasure", "historyMeasure"})
public class Person implements Serializable {

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @NotNull
    @Column(nullable = false)
    @JsonProperty(required = true)
    private String firstName;

    @NotNull
    @Column(nullable = false)
    @JsonProperty(required = true)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;

    @NotNull
    @Transient
    @JacksonXmlElementWrapper(localName = "currentMeasures")
    @JsonProperty(required = true)
    private List<Measure> currentMeasure = new ArrayList<>();

    @Transient
    @JacksonXmlElementWrapper(localName = "historyMeasures")
    @JsonProperty
    private List<Measure> historyMeasure = new ArrayList<>();

    public Person() {
    }

    public Person(String firstName, String lastName, Date birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Measure> getCurrentMeasure() {
        return currentMeasure;
    }

    public void setCurrentMeasure(List<Measure> currentMeasure) {
        this.currentMeasure = currentMeasure;
    }

    public List<Measure> getHistoryMeasure() {
        return historyMeasure;
    }

    public void setHistoryMeasure(List<Measure> historyMeasure) {
        this.historyMeasure = historyMeasure;
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
                    && Objects.equals(birthday, person.birthday);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthday);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("birthday", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(birthday))
                .add("currentMeasure", currentMeasure == null ? "null" : Arrays.toString(currentMeasure.toArray()))
                .add("historyMeasure", historyMeasure == null ? "null" : Arrays.toString(historyMeasure.toArray()))
                .toString();
    }
}
