package org.example

import kotlin.concurrent.thread

fun getUserFromNetworkCallback(userId: String, onUserReady: (User) -> Unit) {
    thread(isDaemon = false) {
        Thread.sleep(1000)
        val thName = Thread.currentThread().name
        val user = User(userId, "Hello")
        println("Thread: $thName")
        onUserReady(user)
    }
    println("fun end")
}

fun main() {
    val thName = Thread.currentThread().name
    getUserFromNetworkCallback("1") { user ->
        println(user)
    }
    println("main Thread: $thName")
    println("main end")
}
