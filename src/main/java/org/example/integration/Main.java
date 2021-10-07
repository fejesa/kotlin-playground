package org.example.integration;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        var sourceExecutor = Executors.newSingleThreadScheduledExecutor(new CustomThreadFactory("source"));
        var sinkExecutor = Executors.newFixedThreadPool(5, new CustomThreadFactory("sink"));

        final Processor processor = new Processor();
        final Consumer consumer = new Consumer();
        sourceExecutor.scheduleAtFixedRate(() -> consumer
            .source()
            .limit(2)
            .forEach(f -> {
                processor.process(f)
                    .thenAcceptAsync(consumer::sink, sinkExecutor)
                    .exceptionally(e -> {
                        System.out.println("Error:" + e.getMessage());
                        return null;
                    });
            }), 0, 5000, TimeUnit.MILLISECONDS);
    }
}

class Consumer {

    private final AtomicLong counter = new AtomicLong();

    public void sink(Response response) {
        var duration = Duration.between(response.getRequest().getTime(), LocalTime.now());
        log("Got Result: " + response + " in (ms): " + duration.toMillis());
    }

    public Stream<Request> source() {
        return Stream.generate(this::createRequest);
    }

    private Request createRequest() {
        var uuid = UUID.randomUUID().toString();
        var index = uuid.indexOf('-');
        var id = counter.incrementAndGet() + "-" + uuid.substring(0, index);
        log("Create Request: " + id);
        return new Request(id, LocalTime.now());
    }

    private void log(Object obj) {
        System.out.println(Thread.currentThread().getName() + " - " + obj);
    }
}

class CustomThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    CustomThreadFactory(String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
            Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-" +
            poolNumber.getAndIncrement() +
            "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
            namePrefix + threadNumber.getAndIncrement(),
            0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
