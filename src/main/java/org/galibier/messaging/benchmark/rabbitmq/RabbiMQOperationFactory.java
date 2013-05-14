package org.galibier.messaging.benchmark.rabbitmq;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;
import org.galibier.messaging.benchmark.TargetGenerator;

import java.util.Arrays;

public class RabbiMQOperationFactory extends OperationFactory {
    public RabbiMQOperationFactory(OperationType type, String host, String queue) {
        super(type, host, queue);
    }

    @Override
    public Operation create() {
        switch (type) {
            case Read:
                return new RabbitMQRead(new TargetGenerator(Arrays.asList(host)), queue);
            case Write:
                return new RabbitMQWrite(new TargetGenerator(Arrays.asList(host)), queue);
            default:
                throw new IllegalStateException("Non supported operation type:" + type);
        }
    }
}
