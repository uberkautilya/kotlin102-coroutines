package io.mcm.five


import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default

/**
 * If a coroutine is launched globally in an android application, it will survive any screen destruction
 * This will continue to work in the background
 * Locally launched coroutines are linked to the screen from which it is launched
 * Actions such as download of file can be in a global scope as we could continue to use the application while it progresses
 */


fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")

    val startTime = System.currentTimeMillis()
    val parentJob = GlobalScope.async {

        //Both the below coroutines are non-blocking
        val resultOne = async {
            return@async getDataOne(Thread.currentThread().name)
        }
        val resultTwo = GlobalScope.async {
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