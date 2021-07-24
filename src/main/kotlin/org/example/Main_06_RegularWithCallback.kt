package org.example

import kotlin.concurrent.thread

private fun getUserFromNetworkCallback(userId: String, onUserReady: (User) -> Unit) {
    thread(isDaemon = false) {
        Thread.sleep(1000)
        val thName = Thread.currentThread().name
        val user = User(userId, "Hello")
        println("Thread fun: $thName")
        onUserReady(user)
    }
    println("fun end")
}

fun main() {

    getUserFromNetworkCallback("1") { user ->
        val thName = Thread.currentThread().name
        println("Thread: $thName, User: $user")
    }
    val thName = Thread.currentThread().name
    println("Thread main: $thName")
    println("main end")
}
