package it.unitn.introsde.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.persistence.entity.Measure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonRootName("measures")
public final class Measures {

    @JsonProperty(value = "measures")
    @JacksonXmlProperty(localName = "measure")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Measure> measures;

    @JsonCreator
    public Measures(@JsonProperty("measure") List<Measure> measures) {
        this.measures = measures;
    }

    public List<Measure> getMeasures() {
        return Collections.unmodifiableList(measures);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Measures) {
            Measures measures = (Measures) o;

            return Objects.equals(this.measures, measures.measures);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(measures);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("measures", Arrays.toString(measures.toArray()))
                .toString();
    }
}