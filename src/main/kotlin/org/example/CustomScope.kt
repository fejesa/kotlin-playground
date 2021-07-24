package org.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CustomScope : CoroutineScope {

    private var parentJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + parentJob

    fun onStart() {
        parentJob = Job()
    }

    fun onStop() {
        val thName = Thread.currentThread().name
        println("Thread - OnStop - $thName")
        parentJob.cancel()
    }
}

fun main() {
    val scope = CustomScope()

    scope.launch {
        val thName = Thread.currentThread().name
        println("Thread - Launch - $thName")
    }

    scope.onStop();

    Thread.sleep(1000)
}
