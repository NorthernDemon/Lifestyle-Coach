package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.ServiceConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "measure_type", schema = ServiceConfiguration.SCHEMA, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"type", "unit"}),
})
@JsonRootName("measureType")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "type", "unit"})
@NamedQueries({
        @NamedQuery(
                name = MeasureType.FIND_BY_TYPE_AND_UNIT,
                query = "SELECT mt FROM MeasureType mt WHERE mt.type = :type AND mt.unit = :unit"
        ),
})
public class MeasureType implements Serializable {

    public static final String FIND_BY_TYPE_AND_UNIT = "MeasureType.findByTypeAndUnit";

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @NotNull
    @NotEmpty
    @Size(max = 30)
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private String type;

    @NotNull
    @NotEmpty
    @Size(max = 30)
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private String unit;

    public MeasureType() {
    }

    public MeasureType(int id) {
        this.id = id;
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
            MeasureType object = (MeasureType) o;

            return Objects.equals(id, object.id)
                    && Objects.equals(type, object.type)
                    && Objects.equals(unit, object.unit);
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