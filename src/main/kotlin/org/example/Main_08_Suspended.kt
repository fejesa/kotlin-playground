package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private suspend fun getUserSuspended(userId: String): User {
    println("Thread fun: ${Thread.currentThread().name}")
    delay(1000)
    println("Thread fun: ${Thread.currentThread().name}")
    delay(1000)
    return User(userId, "Hello")
}

@DelicateCoroutinesApi
fun main() {
    GlobalScope.launch {
        val user = getUserSuspended("1")
        println("Thread launch: ${Thread.currentThread().name} $user")
    }

    println("Thread main: ${Thread.currentThread().name}")
    Thread.sleep(3000)
}
