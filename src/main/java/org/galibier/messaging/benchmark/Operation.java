package org.galibier.messaging.benchmark;

public interface Operation {
    public void initialize();
    public boolean execute();
    public void terminate();
}
