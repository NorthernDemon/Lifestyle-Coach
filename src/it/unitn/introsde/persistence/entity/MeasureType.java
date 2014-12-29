package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.ServiceConfiguration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "measure_type", schema = ServiceConfiguration.SCHEMA)
@JsonRootName("measureType")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "type", "unit"})
public class MeasureType implements Serializable {

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @NotNull
    @Size(max = 30)
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private String type;

    @NotNull
    @Size(max = 30)
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private String unit;

    public MeasureType() {
    }

    public MeasureType(String type, String unit) {
        this.type = type;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof MeasureType) {
            MeasureType measure = (MeasureType) o;

            return Objects.equals(id, measure.id)
                    && Objects.equals(type, measure.type)
                    && Objects.equals(unit, measure.unit);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, unit);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("unit", unit)
                .toString();
    }
}