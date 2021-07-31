package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

fun main() = runBlocking {
    val job = launch {
        repeat(1000) {
            i ->
            println("${LocalTime.now()} $i. Number...")
            delay(500)
        }
    }
    delay(1300)
    println("${LocalTime.now()} main: Stop waiting")
    job.cancel()
    job.join()
    println("${LocalTime.now()} main: Stop")
}
