package org.galibier.messaging.benchmark;

import org.galibier.messaging.benchmark.rabbitmq.RabbitMQRead;
import org.galibier.messaging.benchmark.rabbitmq.RabbitMQWrite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiQueueBench {
    private final long duration;
    private final int interval;
    private final int threadCount;
    private final int readerCount;
    private final int writerCount;
    private final ExecutorService service;
    private final ScheduledExecutorService timer;
    private final CountDownLatch latch;
    private final List<Operation> readOperations;
    private final List<Operation> writeOperations;
    private final List<Snapshot> readSnapshots;
    private final List<Snapshot> writeSnapshots;

    public MultiQueueBench(long duration, int interval, List<RabbitMQRead> readers, List<RabbitMQWrite> writers) {
        this.duration = duration;
        this.interval = interval;
        this.readerCount = readers.size();
        this.writerCount = writers.size();
        this.threadCount = readerCount + writerCount;

        this.service = Executors.newFixedThreadPool(threadCount);
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.latch = new CountDownLatch(threadCount + 1);

        this.readOperations = new ArrayList<Operation>(readers);
        this.writeOperations = new ArrayList<Operation>(writers);

        this.readSnapshots = new ArrayList<Snapshot>(readerCount);
        this.writeSnapshots = new ArrayList<Snapshot>(writerCount);
    }

    private void createOperations(List<Operation> operations, List<Snapshot> snapshots) {
        for (int i = 0; i < operations.size(); i++) {
            final Operation operation = operations.get(i);
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
    }


    public void start() {
        createOperations(readOperations, readSnapshots);
        createOperations(writeOperations, writeSnapshots);

        try {
            latch.countDown();
            latch.await();

            timer.scheduleAtFixedRate(new Runnable() {
                long startTime = System.currentTimeMillis();
                @Override
                public void run() {
                    long writes = getDifferenceDuringInterval(writeSnapshots);
                    long reads = getDifferenceDuringInterval(readSnapshots);
                    long timeElapsed = System.currentTimeMillis() - startTime;
                    System.out.println("Time:" + timeElapsed + "\tWrites:" + writes + "\tReads:" + reads);
                }
            }, interval, interval, TimeUnit.SECONDS);

            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted, then exit");
            System.exit(1);
        }
    }

    public long getDifferenceDuringInterval(List<Snapshot> snapshots) {
        long total = 0;
        for (Snapshot snapshot: snapshots) {
            total += snapshot.take();
        }

        return total;
    }

    public void stop() {
        timer.shutdownNow();
        service.shutdownNow();

        for (Operation operation: writeOperations) {
            operation.terminate();
        }

        for (Operation operation: readOperations) {
            operation.terminate();
        }
    }

}
