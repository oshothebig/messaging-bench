package org.galibier.messaging.benchmark.rabbitmq;

import org.galibier.messaging.benchmark.Message;

import java.io.IOException;

public class RabbitMQWrite extends RabbitMQOperation {
    public RabbitMQWrite(String host, String queue) {
        super(host, queue);
    }

    @Override
    public boolean execute() {
        try {
            channel.basicPublish("", queue, null, Message.getDefault());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
