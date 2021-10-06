package org.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun main() {
    val messages = flow<String> {
        for (number in 0..100) {
            emit("Message: $number")
            delay(100)
        }
    }

    GlobalScope.launch {
        messages
            .map { it.split(" ") }
            .map { it.last() }
            .onEach { 10 }
            .debounce(5)
            .collect { value ->  println(value)}
    }

    Thread.sleep(1000)
}
