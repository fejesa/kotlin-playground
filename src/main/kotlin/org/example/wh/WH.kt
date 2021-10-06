package org.example.wh

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

data class WHRequest(val id: String)

data class WHResult(val request: WHRequest, var result: Int)

class Processor {

    private val channel = Channel<Pair<WHRequest, CompletableFuture<WHResult>>>()

    init {
        val receiveJob = GlobalScope.launch(Dispatchers.IO) {
            try {
                while (!channel.isClosedForReceive) {
                    val item = channel.receive()
                    process(item)
                }
            } catch (e: Throwable) {
                println("Error: ${e.message}")
            }
        }
    }

    fun process(request: WHRequest): CompletableFuture<WHResult> {
        val item = Pair(request, CompletableFuture<WHResult>())
        channel.trySend(item)

        val future = GlobalScope.future {
            item.second.await()
        }
        return future

    }

    private suspend fun process(item: Pair<WHRequest, CompletableFuture<WHResult>>) {
        val request = item.first
        val result = item.second
        println("${java.lang.Thread.currentThread().name} - Process: $request")
        result.complete(WHResult(request, 100))
    }
}
