/*
 * Copyright (c) Fujitsu Laboratories Limited 2013
 *
 * Author: Sho SHIMIZU
 */

package org.galibier.messaging.benchmark;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class CmdParam {
    @Option(name="-t", aliases="--threads", usage="number of threads")
    private int threadCount = 1;
    @Option(name="-h", aliases="--host", usage="target host")
    private String host = "";
    @Option(name="-d", aliases="--duration", usage="duration (sec)")
    private long duration = 30;
    @Option(name="-i", aliases="--interval", usage="report interval (sec)")
    private int interval = 1;
    @Argument(required=true, index=0, usage="type of operation (read / write)")
    private OperationType operationType;
    @Argument(required=true, index=1, usage="type of target (rabbitmq / zookeeper)")
    private TargetType targetType;

    public int getThreadCount() {
        return threadCount;
    }

    public String getHost() {
        if (!host.isEmpty()) {
            return host;
        }

        switch (targetType) {
            case Zookeeper:
                return "localhost:2181";
            case RabbitMQ:
                return "localhost:5672";
            case NOP:
                return host;
            default:
                return host;
        }
    }

    public long getDuration() {
        return duration;
    }

    public int getInterval() {
        return interval;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public TargetType getTargetType() {
        return targetType;
    }
}
