package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kudos {
    @JsonProperty("_id")
    public String id;

    @JsonProperty
    public String topic;

    @JsonProperty
    public String date;

    @JsonProperty
    public String place;

    @JsonProperty
    public String content;

    @JsonProperty
    public Integer targetId;

    @JsonProperty
    public Integer sourceId;

    @JsonIgnore
    public void fromString(String msg) throws IOException {
        JsonNode json = Json.mapper().readTree(msg);
        Kudos kudos = Json.mapper().treeToValue(json, Kudos.class);
        this.id = kudos.id;
        this.topic = kudos.topic;
        this.date = kudos.date;
        this.place = kudos.place;
        this.content = kudos.content;
        this.targetId = kudos.targetId;
        this.sourceId = kudos.sourceId;
    }
//    public Document toDocument() {
//        Document doc = new Document("_id", this.id)
//                .append("topic", this.topic)
//                .append("date", this.date.toString())
//                .append("place", this.place)
//                .append("content", this.content)
//                .append("targetId", this.targetId)
//                .append("sourceId", this.sourceId);
//
//        return doc;
//    }

//    public void fromDocument(Document doc) {
//        this.id = doc.getString("_id");
//        this.topic = doc.getString("topic");
//        this.date = ZonedDateTime.parse(doc.getString("date"));
//        this.place = doc.getString("place");
//        this.content = doc.getString("content");
//        this.targetId = doc.getInteger("targetId");
//        this.sourceId = doc.getInteger("sourceId");
//    }
}
