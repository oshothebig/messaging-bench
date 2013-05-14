package org.galibier.messaging.benchmark.rabbitmq;

import com.rabbitmq.client.Channel;
import org.galibier.messaging.benchmark.Message;
import org.galibier.messaging.benchmark.TargetGenerator;

import java.io.IOException;

public class RabbitMQWrite extends RabbitMQOperation {
    public RabbitMQWrite(TargetGenerator generator, String queue) {
        super(generator, queue);
    }

    @Override
    public boolean execute() {
        try {
            String host = generator.next();
            Channel channel = channels.get(host);
            channel.basicPublish("", queue, null, Message.getDefault());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
