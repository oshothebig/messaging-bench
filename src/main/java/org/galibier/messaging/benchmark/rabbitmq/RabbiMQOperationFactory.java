package org.galibier.messaging.benchmark.rabbitmq;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;

public class RabbiMQOperationFactory extends OperationFactory {
    private final OperationType type;
    private final String host;

    public RabbiMQOperationFactory(OperationType type, String host) {
        this.type = type;
        this.host = host;
    }

    @Override
    public Operation create() {
        switch (type) {
            case Read:
                return new RabbitMQRead(host, "bench");
            case Write:
                return new RabbitMQWrite(host, "bench");
            default:
                throw new IllegalStateException("Non supported operation type:" + type);
        }
    }
}
