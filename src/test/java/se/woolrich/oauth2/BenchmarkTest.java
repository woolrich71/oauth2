package se.woolrich.oauth2;


import org.openjdk.jmh.annotations.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.IntStream;


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"})

public class BenchmarkTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());



    public int doubleIt(int n) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
        return n * 2;
    }

    @Benchmark
    public int doubleAndSumSequential() {
        cache = new ConcurrentHashMap<>();
        return IntStream.of(3, 1, 4, 1, 5, 9)
                .map(e -> getUUid(e).getNow("").length())
                .sum();
    }

    @Benchmark
    public int doubleAndSumParallel() {
        cache = new ConcurrentHashMap<>();
        return IntStream.of(3, 1, 4, 1, 5, 9)
                .parallel()
                .map(e -> getUUid(e).getNow("").length())
                .sum();
    }


    public CompletableFuture<String> getUUid(int id) {

        try {
            String uuid = getFast(id);
            if(uuid != null) {
     //           logger.info("getfast with id=" + id);
               return CompletableFuture.completedFuture(uuid);

            } else {
                CompletableFuture<String> future = new CompletableFuture<>();
     //           logger.info("getSlow with id=" + id);
                uuid = getSlow(id);
                cache.put(id, uuid);
                future.complete(uuid);
                return future;
            }
        } catch (Exception e) {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }


    }


    private Map<Integer, String> cache = new ConcurrentHashMap<>();

    private String getFast(int id) {
        return cache.get(id);
    }

    private String getSlow(int id) {

        try {
            Thread.sleep(100);
            if (id == 666) {
                throw new RuntimeException("Evil request");
            }
        } catch (InterruptedException ignored) {

        }

        return UUID.randomUUID().toString();

    }


}
