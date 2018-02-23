package se.woolrich.oauth2;


import org.openjdk.jmh.annotations.*;
import se.woolrich.bean.Util;

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

    public static final int INT = 100000;
    private Logger logger = Logger.getLogger(this.getClass().getName());



    public int doubleIt(int n) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
        return n * 2;
    }

    //@Benchmark
    public int doubleAndSumSequential() {
        cache = new ConcurrentHashMap<>();
        return IntStream.of(3, 1, 4, 1, 5, 9)
                .map(e -> getUUid(e).getNow("").length())
                .sum();
    }

    //@Benchmark
    public int doubleAndSumParallel() {
        cache = new ConcurrentHashMap<>();
        return IntStream.of(3, 1, 4, 1, 5, 9)
                .parallel()
                .map(e -> getUUid(e).getNow("").length())
                .sum();
    }
/*
    INT = 100000
    Benchmark                        Mode  Cnt  Score    Error  Units
    BenchmarkTest.prepare            avgt   40  5.100 ±  0.069  ms/op
    BenchmarkTest.prepareImperative  avgt   40  0.395 ±  0.004  ms/op
    BenchmarkTest.resolve            avgt   40  4.920 ±  0.056  ms/op
    BenchmarkTest.resolveImperative  avgt   40  0.002 ±  0.001  ms/op
*/

    @Benchmark
    public void resolve() {
        Util util = new Util();
        for(int i = 0; i < INT; i++)
            util.resolve( util.bean(), null);

    }

    @Benchmark
    public void resolveImperative() {
        Util util = new Util();
        for(int i = 0; i <INT; i++)
            util.resolveImperative( util.bean(), null);

    }

    @Benchmark
    public void prepareImperative() {
        Util util = new Util();
        for(int i = 0; i <INT; i++)
            util.prepareImperative( util.bean(), null);

    }

    @Benchmark
    public void prepare() {
        Util util = new Util();
        for(int i = 0; i <INT; i++)
            util.prepare( util.bean(), null);

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
