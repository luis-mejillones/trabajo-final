package model;

import com.datastax.driver.core.Row;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "kudos", schema = "omega@cassandra_pu")
public class Kudos {
    @JsonProperty
    @Id
    @Column(name="id")
    public String id;

    @JsonProperty
    @Column(name="topic")
    public String topic;

    @JsonProperty
    @Column(name="date")
    public String date;

    @JsonProperty
    @Column(name="place")
    public String place;

    @JsonProperty
    @Column(name="content")
    public String content;

    @JsonProperty
    @Column(name="target_id")
    public String targetId;

    @JsonProperty
    @Column(name="source_id")
    public String sourceId;

//    @JsonIgnore
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

//    @JsonIgnore
//    public void fromDocument(Document doc) {
//        this.id = doc.getString("_id");
//        this.topic = doc.getString("topic");
//        this.date = ZonedDateTime.parse(doc.getString("date"));
//        this.place = doc.getString("place");
//        this.content = doc.getString("content");
//        this.targetId = doc.getInteger("targetId");
//        this.sourceId = doc.getInteger("sourceId");
//    }

    public void fromRow(Row row) {
        this.id = row.getString("id");
        this.topic = row.getString("topic");
        this.date = row.getString("date");
        this.place = row.getString("place");
        this.content = row.getString("content");
        this.targetId = row.getString("target_id");
        this.sourceId = row.getString("source_id");

    }

    @JsonIgnore
    public String toString() {
        JsonNode content =  Json.toJson(this);

        return content.toString();
    }
}
