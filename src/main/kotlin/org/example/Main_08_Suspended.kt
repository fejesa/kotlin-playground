package org.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private suspend fun getUserSuspended(userId: String): User {
    delay(1000)
    val thName = Thread.currentThread().name
    println("Thread fun: $thName")
    return User(userId, "Hello")
}

fun main() {
    GlobalScope.launch {
        val thName = Thread.currentThread().name
        val user = getUserSuspended("1")
        println("Thread launch: $thName $user")
    }

    val thName = Thread.currentThread().name
    println("Thread main: $thName")
    Thread.sleep(3000)
}
