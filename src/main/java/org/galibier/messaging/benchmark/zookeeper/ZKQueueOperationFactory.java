package org.galibier.messaging.benchmark.zookeeper;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;
import org.galibier.messaging.benchmark.nop.NoOperation;

public class ZKQueueOperationFactory extends OperationFactory {
    public ZKQueueOperationFactory(OperationType type, String host, String queue) {
        super(type, host, queue);
    }

    @Override
    public Operation create() {
        switch (type) {
            case Read:
                return new ZKQueueRead(host, queue);
            case Write:
                return new ZKQueueWrite(host, queue);
            default:
                return new NoOperation();
        }
    }
}
