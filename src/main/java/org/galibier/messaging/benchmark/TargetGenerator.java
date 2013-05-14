package org.galibier.messaging.benchmark;

import java.util.ArrayList;
import java.util.List;

public class TargetGenerator {
    private final List<String> targets;
    private int currentIndex;

    public TargetGenerator(List<String> targets) {
        if (targets.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.targets = new ArrayList<String>(targets);
        this.currentIndex = 0;
    }

    public List<String> getTargets() {
        return new ArrayList<String>(targets);
    }

    public String next() {
        String next = targets.get(currentIndex);
        if (currentIndex == targets.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }

        return next;
    }
}
