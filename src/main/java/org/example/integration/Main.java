package org.example.integration;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        var sourceExecutor = Executors.newSingleThreadScheduledExecutor();
        var sinkExecutor = Executors.newFixedThreadPool(5);

        final Processor processor = new Processor();
        final Consumer consumer = new Consumer();
        sourceExecutor.scheduleAtFixedRate(() -> consumer
            .source()
            .limit(100)
            .forEach(f -> {
                processor.process(f)
                    .thenAcceptAsync(consumer::sink, sinkExecutor)
                    .exceptionally(e -> {
                        System.out.println("Error:" + e.getMessage());
                        return null;
                    });
            }), 0, 200, TimeUnit.MILLISECONDS);
    }
}

class Consumer {

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
        var id = uuid.substring(0, index);
        log("Create Request: " + id);
        return new Request(id, LocalTime.now());
    }

    private void log(Object obj) {
        System.out.println(Thread.currentThread().getName() + " - " + obj);
    }
}
