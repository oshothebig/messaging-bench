package org.galibier.messaging.benchmark.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.TargetGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class RabbitMQOperation implements Operation {
    protected final TargetGenerator generator;
    protected final String queue;
    protected Map<String, Connection> connections;
    protected Map<String, Channel> channels;

    protected RabbitMQOperation(TargetGenerator generator, String queue) {
        this.generator = generator;
        this.queue = queue;
        this.connections = new HashMap<String, Connection>();
        this.channels = new HashMap<String, Channel>();
    }

    @Override
    public void initialize() {
        try {
            for (String host: generator.getTargets()) {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(host.split(":")[0]);
                factory.setPort(Integer.parseInt(host.split(":")[1]));

                Connection connection = factory.newConnection();
                connections.put(host, connection);

                Channel channel = connection.createChannel();
                channel.queueDeclare(queue, false, false, false, null);
                channels.put(host, channel);
            }
        } catch (IOException e) {
            System.out.println("Initialization failed");
            System.exit(1);
        }
    }

    @Override
    public void terminate() {
        try {
            for (Channel channel: channels.values()) {
                channel.close();
            }
            for (Connection connection: connections.values()) {
                connection.close();
            }
        } catch (IOException e) {
            System.out.println("Termination failed");
        }
    }
}
