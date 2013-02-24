package org.galibier.messaging.benchmark.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.galibier.messaging.benchmark.Operation;

import java.io.IOException;

public abstract class RabbitMQOperation implements Operation {
    protected final String host;
    protected final String queue;
    protected Connection connection;
    protected Channel channel;

    protected RabbitMQOperation(String host, String queue) {
        this.host = host;
        this.queue = queue;
    }

    @Override
    public void initialize() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host.split(":")[0]);
            factory.setPort(Integer.parseInt(host.split(":")[1]));

            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
        } catch (IOException e) {
            System.out.println("Initialization failed");
            System.exit(1);
        }
    }

    @Override
    public void terminate() {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Termination failed");
        }
    }
}
