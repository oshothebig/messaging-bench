package org.galibier.messaging.benchmark.rabbitmq;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;
import org.galibier.messaging.benchmark.TargetGenerator;

import java.util.Arrays;
import java.util.List;

public class RabbiMQOperationFactory extends OperationFactory {
    private final TargetGenerator generator;

    public RabbiMQOperationFactory(OperationType type, List<String> hosts, String queue) {
        super(type, hosts.get(0), queue);

        this.generator = new TargetGenerator(hosts);
    }

    @Override
    public Operation create() {
        switch (type) {
            case Read:
                return new RabbitMQRead(generator, queue);
            case Write:
                return new RabbitMQWrite(generator, queue);
            default:
                throw new IllegalStateException("Non supported operation type:" + type);
        }
    }
}
