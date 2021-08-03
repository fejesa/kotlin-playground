package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {

    val itemNums = 10
    fun produceNumbers(num: Int) = List<Int>(num) { Random.nextInt(0, 1000) }
    val numbersChannel = Channel<Int>(itemNums)

    launch(Dispatchers.Default) {
        produceNumbers(10).forEachIndexed { i, e ->
            val result = numbersChannel.trySend(e)
            println("Send ($i): $e, result: $result  - ${Thread.currentThread().name}")

            if (result.isClosed) {
                println("Sending failed")
            }
        }

        numbersChannel.cancel()
    }

    while(!numbersChannel.isClosedForReceive) {
        val result = numbersChannel.tryReceive()
        println("Receive result: $result - ${java.lang.Thread.currentThread().name}")
    }
}
