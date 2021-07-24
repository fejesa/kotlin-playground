package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

suspend fun <T : Any> getValue(provider: () -> T): T =
    suspendCoroutine { continuation ->
        continuation.resumeWith(Result.runCatching { provider() })
    }

fun execBackground(action: suspend () -> Unit) {
    GlobalScope.launch {
        val thName = Thread.currentThread().name
        println("Thread execBackground: $thName")
        action()
    }
}

fun execMain(action: suspend () -> Unit) {
    GlobalScope.launch(context = Dispatchers.Main) {
        val thName = Thread.currentThread().name
        println("Thread execMain: $thName")
        action()
    }
}

private fun getUser(userId: String): User {
    return User(userId, "Hello")
}

fun main() {

    execBackground {
        val user = getValue { getUser("Background") }
        println(user)
    }

//    execMain {
//        val user = getValue { getUser("Main") }
//        println(user)
//    }

    Thread.sleep(1000)
}
