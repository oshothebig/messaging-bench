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

        OperationFactory readerFactory = null;
        OperationFactory writerFactory = null;
        int readerCount = 0;
        int writerCount = 0;
        switch (param.getOperationType()) {
            case Read:
                readerFactory = OperationFactory.getFactory(param.getTargetType(), param.getOperationType(), param.getHosts(), param.getTarget());
                readerCount = param.getClientCount();
                break;
            case Write:
                writerFactory = OperationFactory.getFactory(param.getTargetType(), param.getOperationType(), param.getHosts(), param.getTarget());
                writerCount = param.getClientCount();
                break;
            case Dual:
                readerFactory = OperationFactory.getFactory(param.getTargetType(), OperationType.Read, param.getHosts(), param.getTarget());
                writerFactory = OperationFactory.getFactory(param.getTargetType(), OperationType.Write, param.getHosts(), param.getTarget());
                readerCount = param.getClientCount();
                writerCount = param.getClientCount();
                break;
            default:
                System.out.println(param.getOperationType() + " is not supported operation");
                System.exit(1);
        }
        Benchmark benchmark = new Benchmark(param.getDuration(), param.getInterval(),
                readerFactory, readerCount, writerFactory, writerCount);

        benchmark.start();
        benchmark.stop();
    }
}