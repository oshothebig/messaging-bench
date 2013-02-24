package org.galibier.messaging.benchmark.nop;

import org.galibier.messaging.benchmark.Operation;
import org.galibier.messaging.benchmark.OperationFactory;

public class NoOperationFactory extends OperationFactory {
    @Override
    public Operation create() {
        return new Operation() {
            @Override
            public void initialize() {
                //  nop
            }

            @Override
            public boolean execute() {
                return true;
            }

            @Override
            public void terminate() {
                //  nop
            }
        };
    }
}
