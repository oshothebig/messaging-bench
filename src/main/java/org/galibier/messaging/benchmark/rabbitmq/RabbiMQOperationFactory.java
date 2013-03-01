package org.galibier.messaging.benchmark.rabbitmq;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;

public class RabbiMQOperationFactory extends OperationFactory {
    public RabbiMQOperationFactory(OperationType type, String host, String queue) {
        super(type, host, queue);
    }

    @Override
    public Operation create() {
        switch (type) {
            case Read:
                return new RabbitMQRead(host, queue);
            case Write:
                return new RabbitMQWrite(host, queue);
            default:
                throw new IllegalStateException("Non supported operation type:" + type);
        }
    }
}
