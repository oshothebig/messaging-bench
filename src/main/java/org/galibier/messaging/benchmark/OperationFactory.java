package org.galibier.messaging.benchmark;

import org.galibier.messaging.benchmark.nop.NoOperationFactory;
import org.galibier.messaging.benchmark.rabbitmq.RabbiMQOperationFactory;
import org.galibier.messaging.benchmark.zookeeper.ZKOperationFactory;

public abstract class OperationFactory {
    public static OperationFactory getFactory(TargetType target, OperationType operation, String host) {
        switch (target) {
            case Zookeeper:
                return new ZKOperationFactory(operation, host);
            case RabbitMQ:
                return new RabbiMQOperationFactory(operation, host);
            case NOP:
                return new NoOperationFactory();
            default:
                break;
        }
        return null;
    }

    public abstract Operation create();
}
