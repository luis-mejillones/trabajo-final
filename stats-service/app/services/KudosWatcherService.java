package services;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Kudos;
import org.bson.Document;
import play.Logger;
import util.Constants;
import util.message.Message;
import util.message.MessageType;

import javax.inject.Inject;

import java.util.concurrent.Executors;

import static com.mongodb.client.model.Filters.eq;

public class KudosWatcherService {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private final KudosMessageSender kudosMessageSender;

    @Inject
    public KudosWatcherService(final KudosMessageSender kudosMessageSender) {
        this.kudosMessageSender = kudosMessageSender;
        this.mongoClient = new MongoClient(Constants.MONGODB_HOST , Constants.MONGODB_PORT );
        this.database = this.mongoClient.getDatabase(Constants.MONGODB_DATABASE);
        this.collection = this.database.getCollection(Constants.MONGODB_COLLECTION);
        this.process();
    }

    private void process() {
//        Executors.newCachedThreadPool().submit(() -> {
//            try {
//                while (true) {
//                    this.checkKudos();
//                    Thread.sleep(1000);
//                }
//            } catch (Exception e) {
//                Logger.error(">>> Error: " + e.getMessage());
//            }
//        });
//
//        Logger.info(">>> Kudos Events watcher is running ...");
    }

    private void checkKudos() {
        MongoCursor<Document> cursor = this.collection.find(eq("messaged", false)).iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Kudos kudos = new Kudos();
                kudos.fromDocument(doc);
                this.sendMessage(MessageType.NEW_KUDOS,kudos.toString());
                kudos.messaged = true;
                Document docToUpdate = kudos.toDocument();
                this.collection.updateOne(eq("_id", kudos.id), new Document("$set", docToUpdate));
            }
        } finally {
            cursor.close();
        }
    }

    public void sendMessage(MessageType type, String message) {
        Message msg = new Message();
        msg.setMessageType(type);
        msg.setContent(message);

        this.kudosMessageSender.send(msg);
    }

}
