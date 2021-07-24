package org.example

import kotlin.concurrent.thread

private fun getUserFromNetworkCallback(
    userId: String,
    onUserResponse: (User?, Throwable?) -> Unit) {
        thread(isDaemon = false) {
            try {
                Thread.sleep(1000)
                val thName = Thread.currentThread().name
                println("Thread: $thName")
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
        val thName = Thread.currentThread().name
        println("Thread callback: $thName")
    }

    val thName = Thread.currentThread().name
    println("Thread main: $thName")
    println("main end")
}
