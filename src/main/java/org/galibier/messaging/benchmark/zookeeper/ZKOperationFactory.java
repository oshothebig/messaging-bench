package org.galibier.messaging.benchmark.zookeeper;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;
import org.galibier.messaging.benchmark.nop.NoOperation;

public class ZKOperationFactory extends OperationFactory {
    public ZKOperationFactory(OperationType type, String host, String queue) {
        super(type, host, queue);
    }

    @Override
    public Operation create() {
        switch (type) {
            case Read:
                return new ZKRead(host, queue);
            case Write:
                return new ZKWrite(host, queue);
            default:
                return new NoOperation();
        }
    }
}
