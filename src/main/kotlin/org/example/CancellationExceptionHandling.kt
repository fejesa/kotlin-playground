package org.example

import kotlinx.coroutines.*

@DelicateCoroutinesApi
fun main() = runBlocking {
    val exHandler = CoroutineExceptionHandler {_, t ->
        println("Caught exception $t")
    }

    val parentJob = GlobalScope.launch(exHandler) {
        println("Parent - ${java.lang.Thread.currentThread().name}")

        val childJob = launch {
            println("Child - ${java.lang.Thread.currentThread().name}")

           launch {
                println("Sub-Child - ${java.lang.Thread.currentThread().name}")

                launch {
                    throw IllegalArgumentException()
                }
            }
        }

        try {
            childJob.join()
        } catch (e: CancellationException) {

            println("Child cancelled: ${childJob.isCancelled}")
            println("Child active: ${childJob.isActive}")

            println("Rethrowing CancellationEx with original")
            throw e
        }
    }

    parentJob.join()

    println("Parent cancelled: ${parentJob.isCancelled}")
    println("Parent active: ${parentJob.isActive}")

}
