package org.example

import kotlin.concurrent.thread

private fun getUserFromNetworkCallback(userId: String, onUserReady: (User) -> Unit) {
    thread(isDaemon = false) {
        Thread.sleep(1000)
        val user = User(userId, "Hello")
        println("Thread fun: ${Thread.currentThread().name}")
        onUserReady(user)
    }
    println("fun end")
}

fun main() {

    getUserFromNetworkCallback("1") { user ->
        println("Thread: ${Thread.currentThread().name}, User: $user")
    }
    println("Thread main: ${Thread.currentThread().name}")
    println("main end")
}
