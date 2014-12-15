package it.unitn.introsde.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.persistence.entity.Person;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonRootName("people")
public final class Persons {

    @JsonProperty("people")
    @JacksonXmlProperty(localName = "person")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Person> persons;

    @JsonCreator
    public Persons(@JsonProperty("person") List<Person> persons) {
        this.persons = persons;
    }

    public List<Person> getPersons() {
        return Collections.unmodifiableList(persons);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Persons) {
            Persons persons = (Persons) o;

            return Objects.equals(this.persons, persons.persons);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("persons", Arrays.toString(persons.toArray()))
                .toString();
    }
}