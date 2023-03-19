package io.mcm.four


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * Coroutine Builder functions: launch, async and runBlocking
 * Async: Can get a value calculated in the coroutine to the main thread
 */

fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")

    val startTime = System.currentTimeMillis()
    val parentJob = CoroutineScope(Default).async {

        //Both the below coroutines are non-blocking
        val resultOne = async {
            return@async getDataOne(Thread.currentThread().name)
        }
        val resultTwo = async {
            getDataTwo(Thread.currentThread().name)
        }
        println("{resultOne.await()}: ${resultOne.await()}, {resultTwo.await()}: ${resultTwo.await()}")

    }
    parentJob.invokeOnCompletion {
        it?.let {
            println("Parent job failed: ${it.message}")
        } ?: println("Parent job and its child jobs has completed")
    }

    //join function can also be called on a deferred object, as it inherits from Job
    runBlocking { parentJob.join() }
    println("Main application end: ${Thread.currentThread().name}")
    println("Time of execution: ${System.currentTimeMillis() - startTime}")
}

//-------------------------------------------------------------------------------------

private suspend fun getDataOne(threadName: String): String {
    println("Work One Simulation: $threadName")
    delay(2000)
    println("Work One End: $threadName")
    return "Result One"
}

private suspend fun getDataTwo(threadName: String): String {
    println("Work Two Simulation: $threadName")
    delay(2000)
    println("Work Two End: $threadName")
    return "Result Two"
}