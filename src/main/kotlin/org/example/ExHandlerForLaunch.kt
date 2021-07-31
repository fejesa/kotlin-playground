package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@DelicateCoroutinesApi
fun main() {
    runBlocking {
        val job = GlobalScope.launch {
            println("Ex from launch")
            throw IllegalArgumentException()
        }

        job.invokeOnCompletion { e -> println("Caught $e") }

        job.join()
    }
    println("Finish")
}
