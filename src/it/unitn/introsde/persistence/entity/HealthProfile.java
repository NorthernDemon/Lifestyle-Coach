package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"weight", "height", "steps"})
public class HealthProfile implements Serializable {

    @Column(nullable = false)
    @JsonProperty(required = true)
    private double weight; // in kilograms

    @Column(nullable = false)
    @JsonProperty(required = true)
    private double height; // in meters

    @Column(nullable = false)
    @JsonProperty(required = true)
    private double steps; // in steps

    public HealthProfile() {
    }

    public HealthProfile(double weight, double height, double steps) {
        this.weight = weight;
        this.height = height;
        this.steps = steps;
    }

    public HealthProfile(HealthProfile healthProfile) {
        this.weight = healthProfile.weight;
        this.height = healthProfile.height;
        this.steps = healthProfile.steps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getSteps() {
        return steps;
    }

    public void setSteps(double steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof HealthProfile) {
            HealthProfile healthProfile = (HealthProfile) o;

            return Objects.equals(weight, healthProfile.weight)
                    && Objects.equals(height, healthProfile.height)
                    && Objects.equals(steps, healthProfile.steps);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, height, steps);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("weight", weight)
                .add("height", height)
                .add("steps", steps)
                .toString();
    }
}