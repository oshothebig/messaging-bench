package org.galibier.messaging.benchmark.nop;

import org.galibier.messaging.benchmark.Operation;

public class NoOperation implements Operation {
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
}
