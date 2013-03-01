package org.galibier.messaging.benchmark;

import org.galibier.messaging.benchmark.nop.NoOperationFactory;
import org.galibier.messaging.benchmark.rabbitmq.RabbiMQOperationFactory;
import org.galibier.messaging.benchmark.zookeeper.ZKOperationFactory;
import org.galibier.messaging.benchmark.zookeeper.ZKQueueOperationFactory;

public abstract class OperationFactory {
    protected final OperationType type;
    protected final String host;
    protected final String queue;

    protected OperationFactory(OperationType type, String host, String queue) {
        if (type != OperationType.Read && type != OperationType.Write) {
            throw new IllegalArgumentException("Non supported operation type:" + type);
        }

        this.type = type;
        this.host = host;
        this.queue = queue;
    }

    public static OperationFactory getFactory(TargetType target, OperationType operation, String host, String queue) {
        switch (target) {
            case Zookeeper:
                return new ZKOperationFactory(operation, host, queue);
            case ZKQueue:
                return new ZKQueueOperationFactory(operation, host, queue);
            case RabbitMQ:
                return new RabbiMQOperationFactory(operation, host, queue);
            case NOP:
                return new NoOperationFactory(operation, host, queue);
            default:
                break;
        }
        return null;
    }

    public abstract Operation create();
}
