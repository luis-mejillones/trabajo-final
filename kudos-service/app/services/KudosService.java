package services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.impetus.client.cassandra.common.CassandraConstants;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import model.Kudos;
import org.bson.Document;
import play.Logger;
import util.Constants;
import util.message.Message;
import util.message.MessageType;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class KudosService {
    private EntityManagerFactory emf;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private final KudosMessageSender kudosMessageSender;
    private Cluster cluster;

    @Inject
    public KudosService(KudosMessageSender kudosMessageSender) {
        this.kudosMessageSender = kudosMessageSender;
        this.mongoClient = new MongoClient(Constants.MONGODB_HOST , Constants.MONGODB_PORT );
        this.database = this.mongoClient.getDatabase(Constants.MONGODB_DATABASE);
        this.collection = this.database.getCollection(Constants.MONGODB_COLLECTION);
    }

    public void close() {
        this.mongoClient.close();
    }

    public Kudos create(Kudos kudos) {
        kudos.id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        kudos.date = ZonedDateTime.now().toString();

        EntityManager em = this.getEmf().createEntityManager();
        em.persist(kudos);
        em.close();

        Logger.info(">>> Kudos created with id: " + kudos.id);
//ToDo Update kudos qty for target user
//        this.sendMessage(MessageType.NEW_KUDOS,kudos.toString());

        return kudos;
    }

    public List<Kudos> getAll() {
        List<Kudos> list = new ArrayList<>();
        try {
            Session session = this.getSession();
            ResultSet rs = session.execute("select * from omega.kudos");
            List<Row> rows = rs.all();
            rows.stream().forEach(row -> {
                Kudos kudos = new Kudos();
                kudos.fromRow(row);
                list.add(kudos);
            });
            Logger.info(">>> Kudos retrieved: " + list.size());
        } finally {
            this.closeSession();
        }

        return list;
    }

    public Kudos getById(String id) {
        EntityManager em = this.getEmf().createEntityManager();
        Kudos kudos = em.find(Kudos.class, id);
        Logger.info("[Get Kudos] Recuperado Kudos con id: " + kudos.id);

        return kudos;
    }

    public List<Document> getByTargetId(Integer id) {
        MongoCursor<Document> cursor = this.collection.find(eq("targetId", id)).iterator();
        List<Document> list = this.retrieveDocuments(cursor);
        Logger.info(">>> Kudos retrieved: " + list.size() + " for user target id: " + id);

        return list;
    }

    public void delete(String id) {
        EntityManager em = this.getEmf().createEntityManager();
        Kudos kudos = em.find(Kudos.class, id);
        em.remove(kudos);
        Logger.info(">>> Kudos delete with id: " + id);
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

    public void deleteByUserId(Integer userId) {
        DeleteResult result = this.collection.deleteMany(eq("targetId", userId));
        Logger.info(">>> Deleting target Kudos for userId: " + userId + " Result: " + result.toString());

        result = this.collection.deleteMany(eq("sourceId", userId));
        Logger.info(">>> Deleting source Kudos for userId: " + userId + " Result: " + result.toString());
    }

    public void sendMessage(MessageType type, String message) {
        Message msg = new Message();
        msg.setMessageType(type);
        msg.setContent(message);

        this.kudosMessageSender.send(msg);
    }

    private EntityManagerFactory getEmf() {
        Map<String, String> props = new HashMap<>();
        props.put(CassandraConstants.CQL_VERSION, CassandraConstants.CQL_VERSION_3_0);

        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("cassandra_pu", props);
        }

        return emf;
    }

    private Session getSession() {
        this.cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .withCredentials("cassandra","cassandra")
                .build();

        return this.cluster.connect();
    }

    private void closeSession() {
        if (this.cluster != null) {
            this.cluster.close();
        }
    }
}
