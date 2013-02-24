package org.galibier.messaging.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Benchmark {
    private final int threadCount;
    private final long duration;
    private final int interval;
    private final OperationFactory factory;
    private final ExecutorService service;
    private final ScheduledExecutorService timer;
    private final CountDownLatch latch;
    private final List<Operation> operations;
    private final List<Snapshot> snapshots;

    public Benchmark(int threadCount, long duration, int interval, OperationFactory factory) {
        this.threadCount = threadCount;
        this.duration = duration;
        this.interval = interval;
        this.factory = factory;
        this.service = Executors.newFixedThreadPool(threadCount);
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.latch = new CountDownLatch(threadCount);
        this.operations = new ArrayList<Operation>(threadCount);
        this.snapshots = new ArrayList<Snapshot>(threadCount);
    }

    public void start() {
        for (int i = 0; i < threadCount; i++) {
            final Operation operation = factory.create();
            operations.add(operation);
            final Snapshot snapshot = new Snapshot();
            snapshots.add(snapshot);

            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        operation.initialize();
                        latch.countDown();
                        latch.await();
                        while (!Thread.currentThread().isInterrupted()) {
                            if (operation.execute()) {
                                snapshot.countUp();
                            }
                        }
                    } catch (InterruptedException e) {
                        //  finish
                    }
                }
            });
        }

        try {
            latch.await();

            timer.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    System.out.println(getDifferenceDuringInterval());
                }
            }, interval, interval, TimeUnit.SECONDS);

            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted, then exit");
            System.exit(1);
        }
    }

    public long getDifferenceDuringInterval() {
        long total = 0;
        for (Snapshot snapshot: snapshots) {
            total += snapshot.take();
        }

        return total;
    }

    public void stop() {
        timer.shutdownNow();
        service.shutdownNow();

        for (Operation operation: operations) {
            operation.terminate();
        }
    }
}
