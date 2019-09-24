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
        this.kudosQty = 0;
    }

    @JsonProperty("_id")
    public String id;

    @JsonProperty
    public String nickname;

    @JsonProperty
    public String fullName;

    @JsonProperty
    public Integer kudosQty;

    @JsonIgnore
    public Document toDocument() {
        Document doc = new Document("_id", this.id)
                .append("nickname", this.nickname)
                .append("fullName", this.fullName)
                .append("kudosQty", this.kudosQty);

        return doc;
    }

    @JsonIgnore
    public void fromDocument(Document doc) {
        this.id = doc.getString("_id");
        this.nickname = doc.getString("nickname");
        this.fullName = doc.getString("fullName");
        this.kudosQty = doc.getInteger("kudosQty");
    }

    @JsonIgnore
    public String toString() {
        JsonNode content =  Json.toJson(this);

        return content.toString();
    }
}
