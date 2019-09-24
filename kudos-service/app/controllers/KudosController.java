package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import model.Kudos;
import org.bson.Document;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.KudosService;

import javax.inject.Inject;
import java.util.List;

public class KudosController extends Controller {
    private final KudosService service;

    @Inject
    public KudosController(final KudosService service) {
        this.service = service;
    }

    public Result create() throws Exception {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        }

        Kudos kudos = Json.mapper().treeToValue(json, Kudos.class);
        Kudos out = this.service.create(kudos);

        JsonNode content = Json.toJson(out);
        return created(content);
    }

    public Result getAll() {
        List<Kudos> list = this.service.getAll();
        JsonNode content =  Json.toJson(list);

        return ok(content);
    }

    public Result getById(String id) {
        Kudos out = this.service.getById(id);
        JsonNode content = Json.toJson(out);

        return ok(content);
    }

    public Result getByTargetId(Integer id) {
        List<Document> list = this.service.getByTargetId(id);
        JsonNode content =  Json.toJson(list);

        return ok(content);
    }

    public Result delete(String id) {
        this.service.delete(id);

        return ok();
    }

    public Result deleteByUserId(String userId) {
        this.service.deleteByUserId(userId);

        return ok();
    }

    public Result testmq() throws Exception {
//        String QUEUE_NAME = "hello";
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        factory.setPort(5672);
//        factory.setUsername("admin");
//        factory.setPassword("12345");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received '" + message + "'");
//        };
//        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

        return ok(" [x] Received ''");
    }
}
