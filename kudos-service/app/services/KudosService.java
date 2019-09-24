package services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
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

    public void deleteByUserId(String userId) {
        EntityManager em = this.getEmf().createEntityManager();

        List<Kudos> list = this.getKudosByUserId(userId, "target_id");
        list.stream().forEach(item -> {
            Kudos kudos = em.find(Kudos.class, item.id);
            em.remove(kudos);
        });
        Logger.info("[Delete Kudos] Borrar kudos para el usuario destino con id: " + userId + " #kudos " + list.size());

        list = this.getKudosByUserId(userId, "source_id");
        list.stream().forEach(item -> {
            Kudos kudos = em.find(Kudos.class, item.id);
            em.remove(kudos);
        });
        Logger.info("[Delete Kudos] Borrar kudos para el usuario origen con id: " + userId + " #kudos " + list.size());
    }

    private List<Kudos> getKudosByUserId(String userId, String field) {
        List<Kudos> list = new ArrayList<>();
        Session session = this.getSession();

        String query = String.format("SELECT * FROM omega.kudos WHERE %s='%s' ALLOW FILTERING;", field, userId);
        ResultSet rs = session.execute(query);
        List<Row> rows = rs.all();
        rows.stream().forEach(row -> {
            Kudos kudos = new Kudos();
            kudos.fromRow(row);
            list.add(kudos);
        });

        return list;
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
