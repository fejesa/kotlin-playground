package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
fun main() {
    GlobalScope.launch {
        println("Hello coroutine!")
        delay(500)
        println("Right back at ya!")
    }

    Thread.sleep(1000)
}
