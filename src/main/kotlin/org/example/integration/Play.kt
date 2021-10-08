package org.example.integration

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import mu.KotlinLogging
import org.apache.http.impl.nio.reactor.IOReactorConfig
import java.util.concurrent.CompletableFuture

val client = HttpClient(Apache) {
//    install(Logging) {
//        logger = Logger.DEFAULT
//        level = LogLevel.ALL
//    }
    engine {
        socketTimeout = 10_000
        connectTimeout = 10_000
        connectionRequestTimeout = 20_000
        threadsCount = 2
        customizeClient {
            setMaxConnTotal(20)
            setMaxConnPerRoute(10)
            disableConnectionState()
            setDefaultIOReactorConfig(
                IOReactorConfig.custom()
                    .setIoThreadCount(1)
                    .build()
            )
        }
        customizeRequest {
            setRedirectsEnabled(false)
            setContentCompressionEnabled(false)
        }
    }
}

@DelicateCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class PlayProcessor(channelCount: Int): Processor {

    private val logger = KotlinLogging.logger {}

    private lateinit var channels: List<Channel<Pair<Request, CompletableFuture<Response>>>>

    private val dispatcher = newFixedThreadPoolContext(5, "process")

    init {
        channels = List(channelCount) { Channel<Pair<Request, CompletableFuture<Response>>>(capacity = Channel.UNLIMITED) }
        for (it in 0 until channelCount) {
            GlobalScope.launch(dispatcher) {
                try {
                    val channel = channels[it]
                    while (!channel.isClosedForReceive) {
                        val item = channel.receive()
                        exec(item)
                    }
                } catch (e: Throwable) {
                    logger.error { "$e" }
                }
            }
        }
    }

    override fun process(request: Request): CompletableFuture<Response> {
        val item = Pair(request, CompletableFuture<Response>())
        val channel = channels[request.rid.id]
        val result = channel.trySend(item)

        if (result.isSuccess) {
            val future = GlobalScope.future(dispatcher) {
                logger.info { "Before await: $request" }
                val t = item.second.asDeferred().await()
                logger.info { "After Await: $request" }
                t
            }
            logger.info { "After scheduled processing: $request" }
            return future
        } else {
            throw RuntimeException()
        }
    }

    private suspend fun exec(item: Pair<Request, CompletableFuture<Response>>) {
        val request = item.first
        val result = item.second

        logger.info{"Enqueue HTTP call: $request" }

        val response: HttpResponse = client.post(request.endpoint) {
            contentType(ContentType.Application.Json)
            body = """{ "id": "${request.getId()}" }"""
        }

        logger.info { "After HTTP call: $request" }
        result.complete(Response(request, Result.success(response.status.value)))
    }
}
