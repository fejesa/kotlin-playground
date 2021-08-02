package org.example

import kotlinx.coroutines.*

@DelicateCoroutinesApi
private fun getUserFromNetwork(userId: String): Deferred<User> = GlobalScope.async {
    Thread.sleep(3000)
    println("Thread fun: ${Thread.currentThread().name}")
    User(userId, "Hello")
}

@DelicateCoroutinesApi
fun main() {
    val userId = "100"

    GlobalScope.launch {
        val user = getUserFromNetwork(userId)
        println("Thread launch: ${Thread.currentThread().name}")
        println(user.await())
    }

    println("Thread main: ${Thread.currentThread().name}")

    Thread.sleep(5000)
}
