package org.galibier.messaging.benchmark.nop;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;
import org.galibier.messaging.benchmark.OperationType;

public class NoOperationFactory extends OperationFactory {
    public NoOperationFactory(OperationType type, String host) {
        super(type, host);
    }

    @Override
    public Operation create() {
        return new NoOperation();
    }

}
