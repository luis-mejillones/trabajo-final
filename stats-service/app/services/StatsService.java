package services;

import play.Logger;
import util.message.Message;
import util.message.MessageType;

import javax.inject.Inject;

public class StatsService {

    private KudosMessageSender kudosMessageSender;

    @Inject
    public StatsService(KudosMessageSender kudosMessageSender) {
        this.kudosMessageSender = kudosMessageSender;
    }

    public void deleteByUserId(String userId) {
        Logger.info("[Delete Kudos] Borrar kudos para user id: " + userId);
        this.sendMessage(MessageType.DELETE_USER, userId);
    }

    public void sendMessage(MessageType type, String message) {
        Message msg = new Message();
        msg.setMessageType(type);
        msg.setContent(message);

        this.kudosMessageSender.send(msg);
    }
}
