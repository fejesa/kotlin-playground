package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

fun main() = runBlocking {
    val jobOne = launch {
        println("${LocalTime.now()} Job 1: started - ${java.lang.Thread.currentThread().name}")
        delay(2000)
        println("${LocalTime.now()} Job 1: finished - ${java.lang.Thread.currentThread().name}")
    }

    val jobSecond = launch {
        println("${LocalTime.now()} Job 2: started - ${java.lang.Thread.currentThread().name}")
        delay(500)
        println("${LocalTime.now()} Job 2: finished - ${java.lang.Thread.currentThread().name}")
    }

    joinAll(jobOne, jobSecond)

    println("Finished")
}
