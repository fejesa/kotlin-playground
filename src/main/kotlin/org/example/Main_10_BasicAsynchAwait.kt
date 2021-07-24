package org.example

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private fun getUserFromNetwork(userId: String): Deferred<User> = GlobalScope.async {
    Thread.sleep(3000)
    val thName = Thread.currentThread().name
    println("Thread fun: $thName")
    User(userId, "Hello")
}

fun main() {
    val userId = "100"

    GlobalScope.launch {
        val user = getUserFromNetwork(userId)
        val thName = Thread.currentThread().name
        println("Thread launch: $thName")
        println(user.await())
    }

    val thName = Thread.currentThread().name
    println("Thread main: $thName")

    Thread.sleep(5000)
}
