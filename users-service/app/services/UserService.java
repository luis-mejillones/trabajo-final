package services;

import akka.japi.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Kudos;
import model.User;
import org.bson.Document;
import org.springframework.util.StringUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Constants;
import util.message.Message;
import util.message.MessageType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.created;


public class UserService {
    private MessageSender messageSender;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private StatsService statsService;

    @Inject
    public UserService(StatsService statsService, MessageSender messageSender) {
        this.statsService = statsService;
        this.messageSender = messageSender;
        this.mongoClient = new MongoClient(Constants.MONGODB_HOST , Constants.MONGODB_PORT );
        this.database = this.mongoClient.getDatabase(Constants.MONGODB_DATABASE);
        this.collection = this.database.getCollection(Constants.MONGODB_COLLECTION);

    }

    public Result create(User user) {
        List<Document> list = this.getByNickname(user.nickname);
        if (list.size() > 0) {
            return badRequest("Nickname repetido.");
        }
        user.id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        Document doc = user.toDocument();
        this.collection.insertOne(doc);
        JsonNode content = Json.toJson(user);
        Logger.info("[Create User] Usuario creado con id: " + user.id);

        return created(content);
    }

    public List<Document> getAll() {
        MongoCursor<Document> cursor = this.collection.find().iterator();
        List<Document> list = this.retrieveDocuments(cursor);
        Logger.info(">>> Users retrieved: " + list.size());

        return list;
    }

    public User getById(String id) {
        User out = new User();
        MongoCursor<Document> cursor = this.collection.find(eq("_id", id)).iterator();
        if (cursor.hasNext()) {
            out.fromDocument(cursor.next());
        }

        Logger.info("[Get User] Recuperar ususario con id: " + out.id);

        return out;
    }

    public List<Document> getByNickname(String nickname) {
        MongoCursor<Document> cursor = this.collection.find(eq("nickname", nickname)).iterator();
        List<Document> list = this.retrieveDocuments(cursor);
        Logger.info("[Get Users] Usuarios recuperados por nickname '" + nickname + "' #" + list.size());

        return list;
    }

    public Pair<User, List<Kudos>> getDetailById(Integer id) {
//        User out = User.find.ref(id);
//        Logger.info("User find by id: " + out.id);
//
//        List<Kudos> kudos = this.kudosService.getKudos(id);

//        return new Pair<>(out, kudos);
        return new Pair<>(new User(), new ArrayList<>());
    }

    public void delete(String id) {
        this.collection.deleteOne(eq("_id", id));
        this.statsService.deleteByUserId(id);
        Logger.info("[Delete User] Usuario borrado id: " + id);
    }

    public List<User> find(String nickname, String name) {
        if (!StringUtils.isEmpty(nickname) && StringUtils.isEmpty(name)) {
            return this.findByNickname(nickname);
        }

        if (StringUtils.isEmpty(nickname) && !StringUtils.isEmpty(name)) {
            return this.findByName(name);
        }

        return this.findByBoth(nickname, name);
    }

    private List<User> findByBoth(String nickname, String name) {
        List<User> list = new ArrayList<>(); //User.find.query().where()
//                .or()
//                .ilike("nickname", "%" + nickname + "%")
//                .ilike("full_name", "%" + name + "%")
//                .endOr()
//                .orderBy("id asc")
//                .setFirstRow(0)
//                .setMaxRows(100)
//                .findPagedList()
//                .getList();

        Logger.info("User find by nickname and name criteria '" + nickname + " or " + name + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }

    private List<User> findByNickname(String nickname) {
        List<User> list = new ArrayList<>(); //User.find.query().where()
//                .ilike("nickname", "%" + nickname + "%")
//                .orderBy("id asc")
//                .setFirstRow(0)
//                .setMaxRows(100)
//                .findPagedList()
//                .getList();

        Logger.info("User find by nickname criteria '" + nickname + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }

    private List<User> findByName(String name) {
        List<User> list = new ArrayList<>(); //User.find.query().where()
//                .ilike("full_name", "%" + name + "%")
//                .orderBy("id asc")
//                .setFirstRow(0)
//                .setMaxRows(100)
//                .findPagedList()
//                .getList();

        Logger.info("User find by name criteria '" + name + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }

    public void sendMessage(MessageType type, String message) {
        Message msg = new Message();
        msg.setMessageType(type);
        msg.setContent(message);

        this.messageSender.send(msg);
    }

    public void updateKudosQty(String msg) {
//        Kudos kudos = new Kudos();
//        try {
//            kudos.fromString(msg);
//        } catch (Exception e) {
//            Logger.error(">>> Error parsing received message: '" + msg + "' " + e.getMessage());
//
//            return;
//        }
//
//        User user = this.getById(kudos.targetId);
//        user.kudosQty++;
////        user.update();
//        Logger.info(">>> Kudos incremented for targetId:" + kudos.targetId + " to " + user.kudosQty);
    }

    private List<Document> retrieveDocuments(MongoCursor<Document> cursor) {
        List<Document> list = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        } finally {
            cursor.close();
        }

        return list;
    }
}
