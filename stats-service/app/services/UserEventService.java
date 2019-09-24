package services;

import model.UserEvent;
import play.Logger;
import util.message.Message;
import util.message.MessageType;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Executors;

public class UserEventService {
    private final UsersMessageSender usersMessageSender;

    @Inject
    public UserEventService(final UsersMessageSender usersMessageSender) {
        this.usersMessageSender = usersMessageSender;
//        this.process();
    }

//    private void process() {
//        Executors.newCachedThreadPool().submit(() -> {
//            try {
//                while (true) {
//                    this.checkEvents();
//                    Thread.sleep(1000);
//                }
//            } catch (Exception e) {
//                Logger.error(">>> Error: " + e.getMessage());
//            }
//        });
//
//        Logger.info(">>> User Events watcher is running ...");
//    }

//    public void checkEvents() {
//        List<UserEvent> list = UserEvent.find.all();
//
//        for (UserEvent item: list) {
//            this.sendMessage(MessageType.valueOf(item.type), item.content);
//            item.delete();
//        }
//    }

    private void sendMessage(MessageType type, String message) {
        Message msg = new Message();
        msg.setMessageType(type);
        msg.setContent(message);

        this.usersMessageSender.send(msg);
    }
}
