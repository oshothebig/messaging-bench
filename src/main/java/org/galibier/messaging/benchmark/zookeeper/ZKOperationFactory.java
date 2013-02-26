package org.galibier.messaging.benchmark.zookeeper;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;
import org.galibier.messaging.benchmark.nop.NoOperation;

public class ZKOperationFactory extends OperationFactory {
    public ZKOperationFactory(OperationType type, String host) {
        super(type, host);
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
