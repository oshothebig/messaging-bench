package org.galibier.messaging.benchmark.zookeeper;

import org.galibier.messaging.benchmark.Message;

public class ZKQueueWrite extends ZKQueueOperation {
    public ZKQueueWrite(String host, String path) {
        super(host, path);
    }

    @Override
    public boolean execute() {
        try {
            queue.offer(Message.getDefault());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
