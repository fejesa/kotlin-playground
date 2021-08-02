package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

@DelicateCoroutinesApi
@ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() {

    val items = arrayListOf("A", "B", "C", "D", "E")
    val channel = Channel<String>()

    runBlocking {
        val sendJob = GlobalScope.launch(Dispatchers.Default) {
            for (item in items) {
                println("${Thread.currentThread().name} - Send: $item")
                channel.send(item)
            }

            channel.close()
        }

        val receiveJob = GlobalScope.launch(Dispatchers.Default) {
            try {
                while (!channel.isClosedForReceive) {
                    println("${java.lang.Thread.currentThread().name} - Receive: ${channel.receive()}")
                    // There is a race condition on the update of the isClosedForReceive property in the kotlin API
                    // delay(10)
                }
            } catch (e: Throwable) {
                println("Error: ${e.message}")
            }
        }

        joinAll(sendJob, receiveJob)

        println("Finish")

    }
}
