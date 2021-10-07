package org.example.integration

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.logging.*
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
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

data class Request(val id: String, val time: LocalTime)

data class Response(val request: Request, var result: Result<Int>)

val client = HttpClient(Apache) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    engine {
        socketTimeout = 10_000
        connectTimeout = 10_000
        connectionRequestTimeout = 20_000
        customizeClient {
            setMaxConnTotal(100)
            setMaxConnPerRoute(200)
            disableConnectionState()
        }
        customizeRequest {
            setRedirectsEnabled(false)
            setContentCompressionEnabled(false)
        }
    }
}

@DelicateCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class Processor {

    private val channel = Channel<Pair<Request, CompletableFuture<Response>>>(capacity = Channel.UNLIMITED)

    private val dispatcher = newFixedThreadPoolContext(5, "process")

    init {
        GlobalScope.launch(dispatcher) {
            try {
                while (!channel.isClosedForReceive) {
                    val item = channel.receive()
                    exec(item)
                }
            } catch (e: Throwable) {
                println("Error: ${e.message}")
            }
        }
    }

    fun process(request: Request): CompletableFuture<Response> {
        val item = Pair(request, CompletableFuture<Response>())
        val result = channel.trySend(item)

        if (result.isSuccess) {
            val future = GlobalScope.future(dispatcher) {
                trace("Before await", request)
                val t = item.second.asDeferred().await()
                trace("After Await", request)
                t
            }
            trace("After scheduled processing", request)
            return future
        } else {
            throw RuntimeException()
        }
    }

    private suspend fun exec(item: Pair<Request, CompletableFuture<Response>>) {
        val request = item.first
        val result = item.second

        trace("Enqueue HTTP call", request)

        val response: HttpResponse = client.post("http://localhost:8080/play") {
            contentType(ContentType.Application.Json)
            body = """{ "id": "${request.id}" }"""
        }
        trace("After HTTP call", request)
        result.complete(Response(request, Result.success(response.status.value)))
    }

    private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

    private fun trace(msg: String, request: Request) {
        println("${LocalTime.now().format(TIME_FORMATTER)} [${java.lang.Thread.currentThread().name}] - $msg: $request")
    }
}
