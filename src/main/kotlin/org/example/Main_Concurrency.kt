package org.example

import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

var counter = 0
var blockingCounter = 0
var syncCounter = 0

fun myFun() {
    ++counter
}

fun myBlockingFun() {
    runBlocking {
        ++blockingCounter
    }
}

@Synchronized
fun mySyncFun() {
    ++syncCounter
}

fun useFun(f: () -> Unit) {
    thread(start = true) {
        for (i in 1..100_000) {
            f.invoke()
        }
        println("Finish t1")
    }

    thread(start = true) {
        for (i in 1..100_000) {
            f.invoke()
        }
        println("Finish t2")
    }
}

fun main() {

    useFun { myFun() }
    useFun { myBlockingFun() }
    useFun { mySyncFun() }

    Thread.sleep(5000)

    println("Counter: $counter")
    println("Blocking counter: $blockingCounter")
    println("Sync counter: $syncCounter")
}
