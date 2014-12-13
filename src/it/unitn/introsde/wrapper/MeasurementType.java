package it.unitn.introsde.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonRootName("measureTypes")
public final class MeasurementType {

    @JsonProperty("measureTypes")
    @JacksonXmlProperty(localName = "measureType")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> measurementTypes;

    @JsonCreator
    public MeasurementType(@JsonProperty(value = "measureTypes") List<String> measurementTypes) {
        this.measurementTypes = measurementTypes;
    }

    public List<String> getMeasurementTypes() {
        return Collections.unmodifiableList(measurementTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof MeasurementType) {
            MeasurementType measurementType = (MeasurementType) o;

            return Objects.equals(measurementTypes, measurementType.measurementTypes);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurementTypes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("measurementTypes", measurementTypes.size())
                .toString();
    }
}