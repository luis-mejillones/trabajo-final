package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "user_event")
public class UserEvent extends Model {
    public static final Finder<Integer, UserEvent> find = new Finder<>(UserEvent.class);

    @Id
    @JsonProperty
    public Integer id;

    @JsonProperty
    public String type;

    @JsonProperty
    public String content;
}
