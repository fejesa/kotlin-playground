package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
fun main() {
    var isDoorOpen = false

    println("Unlocking the door... please wait.\n")
    GlobalScope.launch {
        delay(3000)

        isDoorOpen = true
    }

    GlobalScope.launch {
        repeat(4) {
            println("Trying to open the door...\n")
            delay(800)

            if (isDoorOpen) {
                println("Opened the door!\n")
            } else {
                println("The door is still locked\n")
            }
        }
    }

    Thread.sleep(5000)
}
