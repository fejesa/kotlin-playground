package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

var counter = 0
var blockingCounter = 0
var syncCounter = 0
var mutexCounter = 0

val mutex = Mutex()

fun myFun() {
    ++counter
}

fun myMutexFun() {
    runBlocking {
        withContext(Dispatchers.Default) {
            mutex.withLock { ++mutexCounter }
        }
    }
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
    useFun { myMutexFun() }

    Thread.sleep(6000)

    println("Counter: $counter")
    println("Blocking counter: $blockingCounter")
    println("Sync counter: $syncCounter")
    println("Mutex counter: $mutexCounter")
}
