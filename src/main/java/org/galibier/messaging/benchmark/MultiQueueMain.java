package org.galibier.messaging.benchmark;

import org.galibier.messaging.benchmark.rabbitmq.RabbitMQRead;
import org.galibier.messaging.benchmark.rabbitmq.RabbitMQWrite;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MultiQueueMain {
    public static void main(String[] args) {
        MultiQueueParams param = new MultiQueueParams();

        CmdLineParser parser = new CmdLineParser(param);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.out);
            System.exit(1);
        }

        int readerCount = param.getCount();
        int writerCount = param.getCount();
        List<String> queues = createUniqueQueueNames(param.getCount());
        List<RabbitMQRead> readers = createReaders(queues, param.getHosts());
        List<RabbitMQWrite> writers = createWriters(queues, param.getHosts());

        MultiQueueBench benchmark = new MultiQueueBench(param.getDuration(), param.getInterval(), readers, writers);

        benchmark.start();
        benchmark.stop();

    }

    private static List<String> createUniqueQueueNames(int count) {
        List<String> queues = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            queues.add(UUID.randomUUID().toString());
        }

        return queues;
    }

    private static List<RabbitMQRead> createReaders(List<String> queues, List<String> hosts) {
        List<RabbitMQRead> readers = new ArrayList<RabbitMQRead>(queues.size());

        for (String queue: queues) {
            RabbitMQRead reader = new RabbitMQRead(new TargetGenerator(hosts), queue);
            readers.add(reader);
        }

        return readers;
    }

    private static List<RabbitMQWrite> createWriters(List<String> queues, List<String> hosts) {
        List<RabbitMQWrite> writers = new ArrayList<RabbitMQWrite>(queues.size());

        for (String queue: queues) {
            RabbitMQWrite writer = new RabbitMQWrite(new TargetGenerator(hosts), queue);
            writers.add(writer);
        }

        return writers;
    }

    private static class MultiQueueParams {
        @Option(name="-c", aliases="--count", usage="number of producers/consumers")
        private int count = 1;
        @Option(name="-h", aliases="--hosts", handler=StringArrayOptionHandler.class, usage="target host(s)")
        private String[] hosts = null;
        @Option(name="-d", aliases="--duration", usage="duration (sec)")
        private long duration = 30;
        @Option(name="-i", aliases="--interval", usage="report interval (sec)")
        private int interval = 1;

        private int getCount() {
            return count;
        }

        public List<String> getHosts() {
            if (hosts == null) {
                return Arrays.asList("localhost:5672");
            } else {
                return Arrays.asList(hosts);
            }
        }

        private long getDuration() {
            return duration;
        }

        private int getInterval() {
            return interval;
        }
    }
}
