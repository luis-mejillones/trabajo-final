package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty
    public Integer id;

    @JsonProperty
    public String nickname;

    @JsonProperty
    public String fullName;

    @JsonProperty
    public Integer kudosQty;
}
