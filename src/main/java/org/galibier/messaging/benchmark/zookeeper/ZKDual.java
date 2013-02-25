/*
 * Copyright (c) Fujitsu Laboratories Limited 2013
 *
 * Author: Sho SHIMIZU
 */

package org.galibier.messaging.benchmark.zookeeper;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.queue.SimpleDistributedQueue;
import com.netflix.curator.retry.RetryOneTime;
import org.galibier.messaging.benchmark.Message;
import org.galibier.messaging.benchmark.Operation;

import java.util.concurrent.*;

public class ZKDual implements Operation {
    private final int count = 2;
    private final int sleepTime = 100;

    private final String host;
    private final String path;
    private final ExecutorService producerThread;
    private final ExecutorService consumerThread;

    private ZKQueueOperation producer;
    private ZKQueueOperation consumer;
    private final CuratorFramework producerClient;
    private final CuratorFramework consumerClient;

    public ZKDual(String host, String path) {
        this.host = host;
        this.path = path;

        this.producerClient = CuratorFrameworkFactory.newClient(host, new RetryOneTime(sleepTime));
        this.producerClient.start();

        this.consumerClient = CuratorFrameworkFactory.newClient(host, new RetryOneTime(sleepTime));
        this.consumerClient.start();

        this.producer = new ZKProducer(producerClient, path);
        this.consumer = new ZKConsumer(consumerClient, path);

        this.producerThread = Executors.newSingleThreadExecutor();
        this.consumerThread = Executors.newSingleThreadExecutor();
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean execute() {
        Future<Boolean> producerResult = producerThread.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return producer.execute();
            }
        });

        Future<Boolean> consumerResult = consumerThread.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return consumer.execute();
            }
        });

        try {
            return consumerResult.get();
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }

    @Override
    public void terminate() {
        producerThread.shutdownNow();
        consumerThread.shutdown();

        producerClient.close();
        consumerClient.close();
    }

    private abstract static class ZKQueueOperation {
        protected final CuratorFramework client;
        protected final String path;
        protected final SimpleDistributedQueue queue;

        protected ZKQueueOperation(CuratorFramework client, String path) {
            this.client = client;
            this.path = path;
            this.queue = new SimpleDistributedQueue(client, path);
        }

        public abstract boolean execute();
    }

    private static class ZKProducer extends ZKQueueOperation {
        protected ZKProducer(CuratorFramework client, String path) {
            super(client, path);
        }

        public boolean execute() {
            try {
                queue.offer(Message.getDefault());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private static class ZKConsumer extends ZKQueueOperation {
        protected ZKConsumer(CuratorFramework client, String path) {
            super(client, path);
        }

        public boolean execute() {
            try {
                queue.take();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
