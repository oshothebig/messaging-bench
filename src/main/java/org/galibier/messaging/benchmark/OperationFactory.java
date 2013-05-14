package org.galibier.messaging.benchmark;

import org.galibier.messaging.benchmark.nop.NoOperationFactory;
import org.galibier.messaging.benchmark.rabbitmq.RabbiMQOperationFactory;
import org.galibier.messaging.benchmark.zookeeper.ZKOperationFactory;
import org.galibier.messaging.benchmark.zookeeper.ZKQueueOperationFactory;

import java.util.List;

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

    public static OperationFactory getFactory(TargetType target, OperationType operation, List<String> hosts, String queue) {
        switch (target) {
            case Zookeeper:
                return new ZKOperationFactory(operation, hosts.get(0), queue);
            case ZKQueue:
                return new ZKQueueOperationFactory(operation, hosts.get(0), queue);
            case RabbitMQ:
                return new RabbiMQOperationFactory(operation, hosts, queue);
            case NOP:
                return new NoOperationFactory(operation, hosts.get(0), queue);
            default:
                break;
        }
        return null;
    }

    public abstract Operation create();
}
