package org.example

fun main() {
    val AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors()
    val c = 64.coerceAtLeast(AVAILABLE_PROCESSORS)
    println(c)

}
