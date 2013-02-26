package org.galibier.messaging.benchmark.zookeeper;

public class ZKQueueRead extends ZKQueueOperation {
    public ZKQueueRead(String host, String path) {
        super(host, path);
    }

    @Override
    public boolean execute() {
        try {
            queue.take();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
