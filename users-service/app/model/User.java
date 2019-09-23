package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.bson.Document;
import play.libs.Json;


@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    public User() {
        this.messaged = Boolean.FALSE;
    }

    @JsonProperty("_id")
    public String id;

    @JsonProperty
    public String nickname;

    @JsonProperty
    public String fullName;

    @JsonProperty
    public Integer kudosQty;

    @JsonProperty
    public Boolean messaged;

    @JsonIgnore
    public Document toDocument() {
        Document doc = new Document("_id", this.id)
                .append("nickname", this.nickname)
                .append("fullName", this.fullName)
                .append("kudosQty", this.kudosQty)
                .append("messaged", this.messaged);

        return doc;
    }

    @JsonIgnore
    public void fromDocument(Document doc) {
        this.id = doc.getString("_id");
        this.nickname = doc.getString("nickname");
        this.fullName = doc.getString("fullName");
        this.kudosQty = doc.getInteger("kudosQty");
        this.messaged = doc.getBoolean("messaged");
    }

    @JsonIgnore
    public String toString() {
        JsonNode content =  Json.toJson(this);

        return content.toString();
    }
}
