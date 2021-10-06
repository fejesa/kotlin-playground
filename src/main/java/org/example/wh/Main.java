package org.example.wh;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.example.integration.Processor;
import org.example.integration.WHRequest;
import org.example.integration.WHResult;

public class Main {

    public static void main(String[] args) {
        var sourceExecutor = Executors.newScheduledThreadPool(2);
        var sinkExecutor = Executors.newFixedThreadPool(2);

        final Processor processor = new Processor();
        final Consumer consumer = new Consumer();
        sourceExecutor.scheduleAtFixedRate(() -> consumer
            .source()
            .limit(5)
            .forEach(f -> {
                processor.process(f)
                    .thenAcceptAsync(consumer::sink, sinkExecutor);
            }), 0, 2, TimeUnit.SECONDS);
    }
}

class Consumer {

    public void sink(WHResult result) {
        log("Got Result: " + result);
    }

    public Stream<WHRequest> source() {
        return Stream.generate(this::createRequest);
    }

    private WHRequest createRequest() {
        var uuid = UUID.randomUUID().toString();
        var index = uuid.indexOf('-');
        var id = uuid.substring(0, index);
        log("Create Request: " + id);
        return new WHRequest(id);
    }

    private void log(Object obj) {
        System.out.println(Thread.currentThread().getName() + " - " + obj);
    }
}
