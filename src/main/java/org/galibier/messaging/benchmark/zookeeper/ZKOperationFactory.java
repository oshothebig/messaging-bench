package org.galibier.messaging.benchmark.zookeeper;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;
import org.galibier.messaging.benchmark.nop.NoOperation;

public class ZKOperationFactory extends OperationFactory {
    private final OperationType type;
    private final String host;

    public ZKOperationFactory(OperationType type, String host) {
        if (type != OperationType.Read && type != OperationType.Write) {
            throw new IllegalArgumentException("Non supported operation type:" + type);
        }

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
            default:
                return new NoOperation();
        }
    }
}
