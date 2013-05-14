/*
 * Copyright (c) Fujitsu Laboratories Limited 2013
 *
 * Author: Sho SHIMIZU
 */

package org.galibier.messaging.benchmark;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.util.Arrays;
import java.util.List;

public class CmdParam {
    @Option(name="-c", aliases="--clients", usage="number of clients")
    private int clientCount = 1;
    @Option(name="-h", aliases="--hosts", handler=StringArrayOptionHandler.class, usage="target host(s)")
    private String[] servers = null;
    @Option(name="-d", aliases="--duration", usage="duration (sec)")
    private long duration = 30;
    @Option(name="-i", aliases="--interval", usage="report interval (sec)")
    private int interval = 1;
    @Option(name="-t", aliases="--target", usage="target of queue")
    private String target = "";
    @Argument(required=true, index=0, usage="type of operation (read / write)")
    private OperationType operationType;
    @Argument(required=true, index=1, usage="type of target (rabbitmq / zookeeper)")
    private TargetType targetType;

    public int getClientCount() {
        return clientCount;
    }

    private String getDefaultHost(TargetType targetType) {
        switch (targetType) {
            case Zookeeper:
                return "localhost:2181";
            case ZKQueue:
                return "localhost:2181";
            case RabbitMQ:
                return "localhost:5672";
            case NOP:
                return "";
            default:
                return "";
        }
    }

    public List<String> getHosts() {
        if (servers == null) {
            return Arrays.asList(getDefaultHost(targetType));
        } else {
            return Arrays.asList(servers);
        }
    }

    public long getDuration() {
        return duration;
    }

    public int getInterval() {
        return interval;
    }

    public String getTarget() {
        if (!target.isEmpty()) {
            return target;
        }

        switch (targetType) {
            case Zookeeper:
                return "/bench";
            case ZKQueue:
                return "/bench";
            case RabbitMQ:
                return "bench";
            case NOP:
                return "";
            default:
                return "";
        }
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public TargetType getTargetType() {
        return targetType;
    }
}
