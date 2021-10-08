package org.example.integration

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlin.random.Random

fun main(args: Array<String>) {

    val rnd = Random
    val serverPort = if(args.isEmpty()) 8080 else Integer.valueOf(args[0])

    embeddedServer(Netty, port = serverPort) {
        routing {
            get("/play") {
                call.respondText("Let's play", status = HttpStatusCode.OK)
            }
            post("/play") {
                println("POST: ${call.receiveText()}")
                //delay(rnd.nextInt(5) * 10L)
                call.respond(HttpStatusCode.Created)
            }
        }
    }.start(wait = true)
}
