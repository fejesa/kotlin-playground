package org.example

import kotlin.concurrent.thread

private fun getUserStandard(userId: String): User {
    Thread.sleep(1000)
    println("Thread fun: ${Thread.currentThread().name}")
    return User(userId, "Hello")
}

fun main() {
    val user = getUserStandard("1");
    println("Thread main: ${Thread.currentThread().name} $user")
    println("main end")
}
