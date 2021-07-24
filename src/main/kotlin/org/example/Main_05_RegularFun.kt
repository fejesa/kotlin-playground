package org.example

import kotlin.concurrent.thread

private fun getUserStandard(userId: String): User {
    Thread.sleep(1000)
    val thName = Thread.currentThread().name
    println("Thread fun: $thName")
    return User(userId, "Hello")
}

fun main() {
    val user = getUserStandard("1");
    val thName = Thread.currentThread().name
    println("Thread main: $thName $user")
    println("main end")
}
