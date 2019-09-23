package services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import play.Logger;
import util.Constants;
import util.message.Message;

import javax.inject.Inject;

public class MessageReceiver {

    private final KudosService kudosService;

    @Inject
    public MessageReceiver(final KudosService kudosService) throws Exception {
        this.kudosService = kudosService;
        Logger.info(">>> MessageReceiver is starting up...");
        this.setup();
    }

    private void setup() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.QUEUE_HOST);
        factory.setPort(Constants.QUEUE_PORT);
        factory.setUsername(Constants.QUEUE_USER_NAME);
        factory.setPassword(Constants.QUEUE_PASSWORD);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(Constants.USERS_QUEUE, false, false, false, null);
        Logger.info(">>> Waiting for messages...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            Logger.info("<<< Received '" + message + "'");
            this.process(message);
        };

        channel.basicConsume(Constants.USERS_QUEUE, true, deliverCallback, consumerTag -> { });
    }

    private void process(String msg) {
        Message message = new Message();
        try {
            message.fromString(msg);
        } catch (Exception e) {
            Logger.error(">>> Error parsing received message: '" + msg + "' " + e.getMessage());

            return;
        }

        switch (message.getMessageType()) {
            case DELETE_USER:
                this.kudosService.deleteByUserId(Integer.parseInt(message.getContent()));
                break;
            default:
                Logger.warn(">>> Unknown message type: '" + message.getMessageType() + "'");
        }
    }
}
