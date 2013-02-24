package org.galibier.messaging.benchmark.rabbitmq;

import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class RabbitMQRead extends RabbitMQOperation {
    private QueueingConsumer consumer;

    public RabbitMQRead(String host, String queue) {
        super(host, queue);
    }

    @Override
    public void initialize() {
        try {
            super.initialize();

            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queue, true, consumer);
        } catch (IOException e) {
            System.out.println("Initialization failed");
        }
    }

    @Override
    public boolean execute() {
        try {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            byte[] bytes = delivery.getBody();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
