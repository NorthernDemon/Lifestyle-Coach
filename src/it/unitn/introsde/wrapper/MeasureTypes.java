package it.unitn.introsde.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonRootName("measureTypes")
public final class MeasureTypes {

    @JsonProperty("measureTypes")
    @JacksonXmlProperty(localName = "measure")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> measureTypes;

    public MeasureTypes() {
    }

    public MeasureTypes(List<String> measureTypes) {
        this.measureTypes = measureTypes;
    }

    public List<String> getMeasureTypes() {
        return Collections.unmodifiableList(measureTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof MeasureTypes) {
            MeasureTypes measureTypes = (MeasureTypes) o;

            return Objects.equals(this.measureTypes, measureTypes.measureTypes);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(measureTypes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("measureTypes", measureTypes.size())
                .toString();
    }
}