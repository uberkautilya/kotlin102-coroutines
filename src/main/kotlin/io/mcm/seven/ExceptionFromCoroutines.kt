package io.mcm.seven

import kotlinx.coroutines.*

/**
 * An exception in any of the child coroutines will exit the parent coroutine, unless handled in a try-catch block
 */
fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")
    val startTime = System.currentTimeMillis()

    val parentJob = CoroutineScope(Dispatchers.Default).launch {

        launch {
            try {
                println(taskOne(Thread.currentThread().name))
            } catch (e: Exception) {
                println("Exception handled: ${e.message}")
            }
        }
        launch {println(taskTwo(Thread.currentThread().name))}
        launch {println(taskThree(Thread.currentThread().name))}
    }
    parentJob.invokeOnCompletion {
        it?.let {println("Parent job failed: ${it.message}")} ?: println("Parent and child jobs COMPLETED")
    }

    runBlocking { parentJob.join() }
    println("Main application end: ${Thread.currentThread().name}")
    println("Time of execution: ${System.currentTimeMillis() - startTime}")
}

//-------------------------------------------------------------------------------------

private suspend fun taskOne(threadName: String): String {
    println("Work One Simulation: $threadName")
    delay(2000) //Can be cancelled throwing a CancellationException
    throw Exception("Simulating exception from taskOne")
}

private suspend fun taskTwo(threadName: String): String {
    println("Work Two Simulation: $threadName")
    delay(3000)
    println("Work Two End: $threadName")
    return "Result Two"
}

private suspend fun taskThree(threadName: String): String {
    println("Work Three Simulation: $threadName")
    delay(3000) //Can be cancelled throwing a CancellationException
    println("Work Three End: $threadName")
    return "Result Three"
}
