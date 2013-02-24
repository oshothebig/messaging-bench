package org.galibier.messaging.benchmark;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CmdParam param = new CmdParam();

        CmdLineParser parser = new CmdLineParser(param);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.out);
            System.exit(1);
        }

        OperationFactory factory = OperationFactory.getFactory(param.getTargetType(), param.getOperationType(), param.getHost());
        Benchmark benchmark = new Benchmark(param.getThreadCount(), param.getDuration(), param.getInterval(), factory);

        benchmark.start();
        benchmark.stop();
    }
}