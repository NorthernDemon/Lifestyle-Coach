package it.unitn.introsde.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import it.unitn.introsde.ServiceConfiguration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "person", schema = ServiceConfiguration.SCHEMA)
@JsonRootName("person")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "name", "surname", "birthday", "facebookId", "googleId"})
@NamedQueries({
        @NamedQuery(
                name = Person.FIND_BY_FACEBOOK_ID,
                query = "SELECT p FROM Person p WHERE p.facebookId = :facebookId"
        ),
        @NamedQuery(
                name = Person.FIND_BY_GOOGLE_ID,
                query = "SELECT p FROM Person p WHERE p.googleId = :googleId"
        ),
})
public class Person implements Serializable {

    public static final String FIND_BY_FACEBOOK_ID = "Person.findByFacebookId";
    public static final String FIND_BY_GOOGLE_ID = "Person.findByGoogleId";

    @Id
    @GeneratedValue
    @JsonProperty(required = true)
    private int id;

    @NotNull
    @Size(max = 20)
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private String name;

    @NotNull
    @Size(max = 20)
    @Column(nullable = false, updatable = false)
    @JsonProperty(required = true)
    private String surname;

    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;

    @Column(nullable = false, unique = true)
    @JsonProperty(required = true)
    private int facebookId;

    @Column(nullable = false, unique = true)
    @JsonProperty(required = true)
    private int googleId;

    public Person() {
    }

    public Person(String name, String surname, Date birthday) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
    }

    public Person(String name, String surname, Date birthday, int facebookId) {
        this(name, surname, birthday);
        this.facebookId = facebookId;
    }

    public Person(String name, String surname, Date birthday, int facebookId, int googleId) {
        this(name, surname, birthday, facebookId);
        this.googleId = googleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(int facebookId) {
        this.facebookId = facebookId;
    }

    public int getGoogleId() {
        return googleId;
    }

    public void setGoogleId(int googleId) {
        this.googleId = googleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Person) {
            Person person = (Person) o;

            return Objects.equals(id, person.id)
                    && Objects.equals(name, person.name)
                    && Objects.equals(surname, person.surname)
                    && Objects.equals(birthday, person.birthday)
                    && Objects.equals(facebookId, person.facebookId);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, birthday, facebookId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("surname", surname)
                .add("birthday", new SimpleDateFormat("yyyy-MM-dd").format(birthday))
                .add("facebookId", facebookId)
                .add("googleId", googleId)
                .toString();
    }
}
