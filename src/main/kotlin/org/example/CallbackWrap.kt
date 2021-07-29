package org.example

import kotlinx.coroutines.runBlocking
import kotlin.coroutines.suspendCoroutine

interface AsyncCallback {

    fun onSuccess(result: String)

    fun onError(e: Exception)

}

fun main() {
    runBlocking {
        try {
            val d = getDataAsync()
            println("Result: $d")
        } catch (e: Exception) {
            println(e)
        }
    }
}

suspend fun getDataAsync(): String {
    return suspendCoroutine { continuation ->
        getData(object: AsyncCallback  {
            override fun onSuccess(result: String) {
                continuation.resumeWith(Result.success(result))
            }

            override fun onError(e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        })
    }
}

fun getData(callback: AsyncCallback) {
    val trigger = true
    try {
        Thread.sleep(3000)

        if (trigger) {
            throw ArithmeticException()
        } else {
            callback.onSuccess("Pi - 3.141592")
        }
    } catch (e: Exception) {
        callback.onError(e)
    }
}
