package org.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private suspend fun getUserSuspended(userId: String): User {
    delay(1000)
    println("Thread fun: ${Thread.currentThread().name}")
    return User(userId, "Hello")
}

fun main() {
    GlobalScope.launch {
        val user = getUserSuspended("1")
        println("Thread launch: ${Thread.currentThread().name} $user")
    }

    println("Thread main: ${Thread.currentThread().name}")
    Thread.sleep(3000)
}
