package it.unitn.introsde.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.persistence.entity.HealthHistory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonRootName("measureHistory")
public final class MeasurementHistory {

    @JsonProperty(value = "measureHistory")
    @JacksonXmlProperty(localName = "measure")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<HealthHistory> measurementHistories;

    @JsonCreator
    public MeasurementHistory(@JsonProperty(value = "measureHistory") List<HealthHistory> measurementHistories) {
        this.measurementHistories = measurementHistories;
    }

    public List<HealthHistory> getMeasurementHistories() {
        return Collections.unmodifiableList(measurementHistories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof MeasurementHistory) {
            MeasurementHistory healthHistory = (MeasurementHistory) o;

            return Objects.equals(measurementHistories, healthHistory.measurementHistories);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurementHistories);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("measurementHistories", measurementHistories.size())
                .toString();
    }
}