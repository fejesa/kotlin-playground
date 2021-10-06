package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

suspend fun <T : Any> getValue(provider: () -> T): T =
    suspendCoroutine { continuation ->
        continuation.resumeWith(Result.runCatching { provider() })
    }

@DelicateCoroutinesApi
fun execBackground(action: suspend () -> Unit) {
    GlobalScope.launch {
        println("Thread execBackground: ${Thread.currentThread().name}")
        action()
    }
}

@DelicateCoroutinesApi
fun execMain(action: suspend () -> Unit) {
    GlobalScope.launch(context = Dispatchers.Main) {
        println("Thread execMain: ${Thread.currentThread().name}")
        action()
    }
}

fun getUser(userId: String): User {
    return User(userId, "Hello")
}

@DelicateCoroutinesApi
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
