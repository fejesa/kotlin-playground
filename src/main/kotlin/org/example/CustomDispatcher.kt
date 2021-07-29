package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@DelicateCoroutinesApi
fun main() {

    val executor = Executors.newWorkStealingPool().asCoroutineDispatcher()

    for (i in 1..10) {
        GlobalScope.launch(context = executor) {
            println("Thread: ${Thread.currentThread().name}")
        }
    }

    Thread.sleep(5000)
}
