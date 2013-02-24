package org.galibier.messaging.benchmark;

public class Snapshot {
    private long total = 0;
    private long previous = 0;

    public synchronized long getTotal() {
        return total;
    }

    public synchronized void countUp() {
        total++;
    }

    public synchronized long take() {
        long difference = total - previous;
        previous = total;
        return difference;
    }
}
