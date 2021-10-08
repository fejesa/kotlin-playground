package org.example.integration

import java.time.LocalTime
import java.util.concurrent.CompletableFuture

data class RequestId(val id: Int, val counter: Int, val uuid: String)

data class Request(val rid: RequestId, val endpoint: String, val time: LocalTime) {
    fun getId() = String.format("%d-%d-%s", rid.id, rid.counter, rid.uuid)
}

data class Response(val request: Request, var result: Result<Int>)

interface Processor {
    fun process(request: Request): CompletableFuture<Response>
}
