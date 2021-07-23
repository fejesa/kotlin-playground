package org.example

import kotlin.concurrent.thread

fun getUserStandard(userId: String): User {
    Thread.sleep(1000)
    return User(userId, "Hello")
}

fun main() {
    val user = getUserStandard("1");
    println(user)
    println("main end")
}
