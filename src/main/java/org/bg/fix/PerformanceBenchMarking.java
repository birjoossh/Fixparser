package org.bg.fix;

import org.bg.fix.models.FixMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
This is a performance benchmarking class that calls teh sync and async api with exponentially increasing load and measure the total and avg elapsed time
 */
public class PerformanceBenchMarking {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java ArgumentParser <maxloadFactor> <fixMessageString> <benchmarkParsingInParallel> <benchmarkParsingInSequence>");
            return;
        }
        try {
            int maxloadFactor = Integer.parseInt(args[0]);
            boolean benchmarkParsingInParallel = Boolean.parseBoolean(args[1]);
            boolean benchmarkParsingInSequence = Boolean.parseBoolean(args[2]);

            System.out.println("maxloadFactor: " + maxloadFactor);
            System.out.println("benchmarkParsingInParallel: " + benchmarkParsingInParallel);
            System.out.println("benchmarkParsingInSequence: " + benchmarkParsingInSequence);

            String fixMessageString = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u0001109=ultrarichclient\u000176=jp\u00011=clientacc\u0001"
                    + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                    + "44=1.67\u000140=1\u000110=21\u0001";

            if (benchmarkParsingInParallel) {
                System.out.println("Benchmarking with multi-threading :");
                System.out.println("|Total Load|Average execution time(nanoseconds)|Total execution time(nanoseconds)|");
                System.out.println("|----------|-----------------------------------|---------------------------------|");
                for (int i = 0; i < maxloadFactor; i++) {
                    int load = (int) Math.pow(10, i);
                    benchmarkParsingInParallel(fixMessageString, load);
                }
            }
            if (benchmarkParsingInSequence) {
                System.out.println("Benchmarking with single-threading :");
                System.out.println("|Total Load|Average execution time(nanoseconds)|Total execution time(nanoseconds)|");
                System.out.println("|----------|-----------------------------------|---------------------------------|");
                for (int i = 0; i < maxloadFactor; i++) {
                    int load = (int) Math.pow(10, i);
                    benchmarkParsingInSequence(fixMessageString, load);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for maxloadFactor.");
        }
    }

    public static void benchmarkParsingInParallel(String fixMessageString, int iterations) {
        long totalTime = 0;

        FixMessageParser parser = new FixMessageParser();
        long startTime = System.nanoTime();
        List<CompletableFuture<FixMessage>> completableFutures = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {

            CompletableFuture<FixMessage> future = parser.parseFixMessageAsynchronous(fixMessageString.getBytes(), false);
            completableFutures.add(future);
        }
        for (int i = 0; i < iterations; i++) {
            try {
                completableFutures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();
        totalTime += (endTime - startTime);

        double averageTime = totalTime / (double) iterations;

        System.out.printf("|%d|%.2f|%.2f|\n", iterations, averageTime, totalTime * 1.0);
        parser.shutdown();
    }

    public static void benchmarkParsingInSequence(String fixMessageString, int iterations) throws Exception {
        long totalTime = 0;

        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            FixMessage fixMessage = FixMessageParser.parseFixMessage(fixMessageString.getBytes(), false);
        }
        long endTime = System.nanoTime();
        totalTime += (endTime - startTime);

        double averageTime = totalTime / (double) iterations;
        System.out.printf("|%d|%.2f|%.2f|\n", iterations, averageTime, totalTime * 1.0);
    }
}
