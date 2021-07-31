package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

fun main() = runBlocking {
    val jobOne = launch {
        delay(2000)
        println("${LocalTime.now()} Job 1: finished - ${java.lang.Thread.currentThread().name}")
    }

    val jobSecond = launch {
        delay(500)
        println("${LocalTime.now()} Job 2: finished - ${java.lang.Thread.currentThread().name}")
    }

    joinAll(jobOne, jobSecond)

    println("Finished")
}
