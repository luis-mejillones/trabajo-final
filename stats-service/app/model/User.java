package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ebean.Finder;
import io.ebean.Model;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "user")
public class User extends Model {
    public static final Finder<Integer, User> find = new Finder<>(User.class);

    @Id
    @JsonProperty
    public Integer id;

    @JsonProperty
    public String nickname;

    @JsonProperty
    public String fullName;

    @JsonProperty
    public Integer kudosQty;
}
