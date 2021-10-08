package org.example.integration

import java.time.LocalTime
import java.util.concurrent.CompletableFuture

data class Request(val id: String, val time: LocalTime)
data class Response(val request: Request, var result: Result<Int>)
data class RequestId(val pid: Int, val counter: Int, val uuid: String)

interface Processor {
    fun process(request: Request): CompletableFuture<Response>
}
