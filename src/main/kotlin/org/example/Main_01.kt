package org.example

import kotlinx.coroutines.*

fun main() {
    GlobalScope.launch {
        println("Hello coroutine!")
        delay(500)
        println("Right back at ya!")
    }

    Thread.sleep(1000)
}
