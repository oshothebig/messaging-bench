package org.galibier.messaging.benchmark.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import org.galibier.messaging.benchmark.TargetGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RabbitMQRead extends RabbitMQOperation {
    private Map<String, QueueingConsumer> consumers;

    public RabbitMQRead(TargetGenerator generator, String queue) {
        super(generator, queue);
        this.consumers = new HashMap<String, QueueingConsumer>();
    }

    @Override
    public void initialize() {
        try {
            super.initialize();

            for (String host: generator.getTargets()) {
                Channel channel = channels.get(host);
                QueueingConsumer consumer = new QueueingConsumer(channel);
                channel.basicConsume(queue, true, consumer);
                consumers.put(host, consumer);
            }
        } catch (IOException e) {
            System.out.println("Initialization failed");
        }
    }

    @Override
    public boolean execute() {
        try {
            String host = generator.next();
            QueueingConsumer.Delivery delivery = consumers.get(host).nextDelivery();
            byte[] bytes = delivery.getBody();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
