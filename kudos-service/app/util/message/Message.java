package util.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.io.IOException;

public class Message {
    private MessageType messageType;
    private String content;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonIgnore
    public String toString() {
        JsonNode content =  Json.toJson(this);

        return content.toString();
    }

    @JsonIgnore
    public void fromString(String msg) throws IOException {
        JsonNode json = Json.mapper().readTree(msg);
        Message message = Json.mapper().treeToValue(json, Message.class);
        this.messageType = message.messageType;
        this.content = message.content;
    }
}
