package org.example.integration;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {

        var batch = 10;
        var period = 5000;

        var sourceExecutor = Executors.newSingleThreadScheduledExecutor(new CustomThreadFactory("source"));
        var sinkExecutor = Executors.newFixedThreadPool(2, new CustomThreadFactory("sink"));

        final Processor processor = new PlayProcessor();
        final Consumer consumer = new Consumer(0);
        sourceExecutor.scheduleAtFixedRate(() -> consumer
            .source()
            .limit(batch)
            .forEach(f -> {
                processor.process(f)
                    .thenAcceptAsync(consumer::sink, sinkExecutor)
                    .exceptionally(e -> {
                        e.printStackTrace();
                        return null;
                    });
            }), 0, period, TimeUnit.MILLISECONDS);
    }
}

class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final AtomicInteger counter = new AtomicInteger();

    private final int cid;

    Consumer(int cid) {
        this.cid = cid;
    }

    public void sink(Response response) {
        var duration = Duration.between(response.getRequest().getTime(), LocalTime.now());
        logger.info("Got Result: {} in {} (ms)", response, duration.toMillis());
    }

    public Stream<Request> source() {
        return Stream.generate(this::createRequest);
    }

    private Request createRequest() {
        var uuid = UUID.randomUUID().toString();
        var index = uuid.indexOf('-');
        var id = new RequestId(cid, counter.incrementAndGet(), uuid.substring(0, index));
        logger.info("Create Request: {}", id);
        return new Request(id, LocalTime.now());
    }
}

class CustomThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    CustomThreadFactory(String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(),0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
