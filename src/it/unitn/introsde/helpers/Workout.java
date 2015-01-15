package it.unitn.introsde.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

import java.util.Objects;

@JsonRootName("workout")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"name", "description"})
public class Workout {

    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String description;

    public Workout() {
    }

    public Workout(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Workout) {
            Workout object = (Workout) o;

            return Objects.equals(name, object.name)
                    && Objects.equals(description, object.description);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("description", description)
                .toString();
    }
}
