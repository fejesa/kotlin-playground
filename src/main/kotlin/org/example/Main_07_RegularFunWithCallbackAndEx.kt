package org.example

import kotlin.concurrent.thread

private fun getUserFromNetworkCallback(
    userId: String,
    onUserResponse: (User?, Throwable?) -> Unit) {
        thread(isDaemon = false) {
            try {
                Thread.sleep(1000)
                println("Thread: ${Thread.currentThread().name}")
                val user = User(userId, "Hi")
                onUserResponse(user, null)
            } catch (error: Throwable) {
                onUserResponse(null, error)
            }
        }
}

fun main() {
    getUserFromNetworkCallback("1") {
        user, error ->
        user?.run (::println)
        error?.printStackTrace()
        println("Thread callback: ${Thread.currentThread().name}")
    }

    println("Thread main: ${Thread.currentThread().name}")
    println("main end")
}
