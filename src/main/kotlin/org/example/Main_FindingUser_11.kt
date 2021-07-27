package org.example

import kotlinx.coroutines.*
import java.io.File

@ObsoleteCoroutinesApi
private fun readUsers(path: String) =
    GlobalScope.async {
        delay(1000)

        println("Thread - ReadUsers - ${Thread.currentThread().name}")

        File(path)
            .readLines()
            .asSequence()
            .filter { it.isNotEmpty() }
            .map {
                val data = it.split(" ")
                if (data.size == 3) data else emptyList()
            }
            .filter {
                it.isNotEmpty()
            }
            .map { User(it[0], it[1])
            }.toList()
    }

private fun checkUserExists(user: User, users: List<User>): Boolean {
    return user in users
}

@ObsoleteCoroutinesApi
private fun getUserFromNetwork(userId: String, parentScope: CoroutineScope): Deferred<User> = GlobalScope.async {
    Thread.sleep(3000)
    println("Thread - Network - ${Thread.currentThread().name}")
    User(userId, "Mike")
}

@ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() {
    val userId = "513";

    val launch = GlobalScope.launch {
        println("Thread - Finding User - ${Thread.currentThread().name}")

        val user = getUserFromNetwork(userId, GlobalScope)
        val users = readUsers(this::class.java.getResource("/users.txt").path)

        val isExist = checkUserExists(user.await(), users.await())

        println("Do other staff")

        if (isExist) println("Found user: ${user.getCompleted()}")
    }

    Thread.sleep(5000)
}
