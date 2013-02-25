package org.galibier.messaging.benchmark.zookeeper;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;

public class ZKOperationFactory extends OperationFactory {
    private final OperationType type;
    private final String host;

    public ZKOperationFactory(OperationType type, String host) {
        this.type = type;
        this.host = host;
    }

    @Override
    public Operation create() {
        switch (type) {
            case Read:
                return new ZKRead(host, "/bench");
            case Write:
                return new ZKWrite(host, "/bench");
            case Dual:
                return new ZKDual(host, "/bench");
            default:
                throw new IllegalStateException("Non supported operation type:" + type);
        }
    }
}
