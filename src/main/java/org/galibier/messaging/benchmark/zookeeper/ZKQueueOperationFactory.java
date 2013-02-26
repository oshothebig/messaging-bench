package org.galibier.messaging.benchmark.zookeeper;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;
import org.galibier.messaging.benchmark.nop.NoOperation;

public class ZKQueueOperationFactory extends OperationFactory {
    public ZKQueueOperationFactory(OperationType type, String host) {
        super(type, host);
    }

    @Override
    public Operation create() {
        switch (type) {
            case Read:
                return new ZKQueueRead(host, "/bench");
            case Write:
                return new ZKQueueWrite(host, "/bench");
            default:
                return new NoOperation();
        }
    }
}
