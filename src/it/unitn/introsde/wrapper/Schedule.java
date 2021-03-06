package it.unitn.introsde.wrapper;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@JsonRootName("schedule")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"startDate", "endDate", "summary", "location"})
public class Schedule implements Serializable {

    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @JsonProperty(required = true)
    private String summary;

    @JsonProperty(required = true)
    private String location;

    public Schedule() {
    }

    public Schedule(Date startDate, Date endDate, String summary, String location) {
        this.endDate = endDate;
        this.startDate = startDate;
        this.summary = summary;
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Schedule) {
            Schedule object = (Schedule) o;

            return Objects.equals(startDate, object.startDate)
                    && Objects.equals(endDate, object.endDate)
                    && Objects.equals(summary, object.summary)
                    && Objects.equals(location, object.location);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, summary, location);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("startDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate))
                .add("endDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate))
                .add("summary", summary)
                .add("location", location)
                .toString();
    }
}
