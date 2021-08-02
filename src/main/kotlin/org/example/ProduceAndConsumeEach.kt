package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
fun main() {

    val items = arrayListOf("A", "B", "C", "D", "E")

    fun produceItems() = GlobalScope.produce<String> {
        for (item in items) {
            println("${Thread.currentThread().name} - Send: $item")
            send(item)
        }

        close()
    }

    runBlocking {
        produceItems().consumeEach { println("${java.lang.Thread.currentThread().name} - Receive: $it") }
        println("Done")
    }
}
