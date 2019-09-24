package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEvent {
    @JsonProperty
    public Integer id;

    @JsonProperty
    public String type;

    @JsonProperty
    public String content;
}
