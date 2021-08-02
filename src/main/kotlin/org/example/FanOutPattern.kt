package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.random.Random

typealias Predicate<E> = (E) -> Boolean
typealias Filter<E> = Pair<Channel<E>, Predicate<E>>

class Multiplexer<E>(private vararg val filters: Filter<E>) {

    suspend fun consume(receive: ReceiveChannel<E>) {
        for (i in receive) {
            for (filter in filters) {
                if (filter.second(i)) {
                    filter.first.send(i)
                }
            }
        }

        cancelAll()
    }

    private fun cancelAll() {
        filters.forEach { it.first.cancel() }
    }
}

@DelicateCoroutinesApi
fun main() {
    fun isDivisibleAll(num: Int, divisors: List<Int>) = divisors.all { num % it == 0 }

    fun produceNumbers(num: Int) = List<Int>(num) { Random.nextInt(0, 1000)}

    runBlocking {
        val numbersChannel = Channel<Int>()

        val evenChannel = Channel<Int>()
        val otherChannel = Channel<Int>()

        GlobalScope.launch(Dispatchers.Default) {
            produceNumbers(50).forEachIndexed {i, e ->
                println("Send ($i): $e - ${Thread.currentThread().name}")
                numbersChannel.send(e)
            }
        }

        val mux = Multiplexer(
            evenChannel to {n: Int -> isDivisibleAll(n, listOf(11))},
            otherChannel to {n: Int -> isDivisibleAll(n, listOf(2, 3, 5))}
        )

        GlobalScope.launch(Dispatchers.Default) {
            mux.consume(numbersChannel)
        }

        GlobalScope.launch(Dispatchers.Default) {
            for (n in evenChannel) {
                println("Received even: $n - ${Thread.currentThread().name}")
            }
        }

        GlobalScope.launch(Dispatchers.Default) {
            for (n in otherChannel) {
                println("Received other: $n - ${Thread.currentThread().name}")
            }
        }
    }

    Thread.sleep(2000)
}
