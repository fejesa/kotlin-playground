package org.example.integration

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlin.random.Random

fun main() {

    val rnd = Random

    embeddedServer(Netty, port = 8080) {
        routing {
            get("/play") {
                println("GET")
                call.respond(HttpStatusCode.OK)
            }
            post("/play") {
                println("POST: ${call.receiveText()}")
                //delay(rnd.nextInt(5) * 10L)
                call.respond(HttpStatusCode.Created)
            }
        }
    }.start(wait = true)
}
