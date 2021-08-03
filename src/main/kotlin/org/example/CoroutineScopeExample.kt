package org.example

import kotlinx.coroutines.*

fun doFirstJob() {
    println("1st job - ${Thread.currentThread().name}")
}

fun doSecondJob() {
    println("2nd job - ${Thread.currentThread().name}")
}

suspend fun doJobs() = coroutineScope {
    val firstJob = launch(Dispatchers.Default) {
        doFirstJob()
        delay(1000)
    }
    val secondJob = launch {
        delay(500)
        doSecondJob()
    }

    //joinAll(firstJob, secondJob)

    println("Done - doJobs")
}

fun main() = runBlocking<Unit> {
    doJobs()
    println("Done - main")
}
