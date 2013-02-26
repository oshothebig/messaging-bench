package org.galibier.messaging.benchmark.zookeeper;

import com.netflix.curator.framework.recipes.queue.SimpleDistributedQueue;

public abstract class ZKQueueOperation extends ZKOperation {
    protected SimpleDistributedQueue queue;

    protected ZKQueueOperation(String host, String path) {
        super(host, path);
    }

    @Override
    public void initialize() {
        client.start();
        queue = new SimpleDistributedQueue(client, path);
    }

    @Override
    public void terminate() {
        client.close();
    }
}
