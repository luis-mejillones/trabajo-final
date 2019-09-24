package services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import play.Logger;
import util.Constants;
import util.message.Message;

public class KudosMessageSender {
    private ConnectionFactory factory;

    public KudosMessageSender() {
        Logger.info("[Queue Service] arrancando el servicio KudosMessageSender...");
        this.setup();
    }

    private void setup() {
        this.factory = new ConnectionFactory();
        factory.setHost(Constants.QUEUE_HOST);
        factory.setPort(Constants.QUEUE_PORT);
        factory.setUsername(Constants.QUEUE_USER_NAME);
        factory.setPassword(Constants.QUEUE_PASSWORD);
    }

    public void send(Message message) {
        String msg = message.toString();
        try {
            try (Connection connection = this.factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(Constants.KUDOS_QUEUE, false, false, false, null);
                channel.basicPublish("", Constants.KUDOS_QUEUE, null, msg.getBytes("UTF-8"));

                Logger.info("[Queue Message] Enviar mensaje: " + msg);
            }
        } catch (Exception e) {
            Logger.error("[Queue Error] " + e.getMessage());
        }
    }
}
