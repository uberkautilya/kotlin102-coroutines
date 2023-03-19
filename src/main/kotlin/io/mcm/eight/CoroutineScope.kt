package io.mcm.eight

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main


/**
 * withContext() Function: To switch from the current thread to another
 */
fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")
    val startTime = System.currentTimeMillis()

    //To start a coroutine lazily, use async(start = CoroutineStart.LAZY) {}
    val parentJob = CoroutineScope(Default).launch {

        val result1 = async(start = CoroutineStart.LAZY) { taskOne(Thread.currentThread().name) }
        val result2 = async(start = CoroutineStart.LAZY) { taskTwo(Thread.currentThread().name) }
        val result3 = async(start = CoroutineStart.LAZY) { taskThree(Thread.currentThread().name) }
        println("{result1.await()}: ${result1.await()}")
//        println("{result2.await()}: ${result2.await()}")
//        println("{result3.await()}: ${result3.await()}")
        result1.cancel()
        result2.cancel()
        result3.cancel()
    }
    parentJob.invokeOnCompletion {
        it?.let { println("Parent job failed: ${it.message}") } ?: println("Parent and child JOBS COMPLETED")
    }


    runBlocking { parentJob.join() }
    println("Main application end: ${Thread.currentThread().name}")
    println("Time of execution: ${System.currentTimeMillis() - startTime}")
}

//-------------------------------------------------------------------------------------

private suspend fun taskOne(threadName: String): String {
    println("Task One start: $threadName")
    delay(2000)
    println("\nTask One End: $threadName")
    return "Result One"
}

private suspend fun taskTwo(threadName: String): String {
    println("Task Two start: $threadName")
    delay(2000)
    println("\nTask Two End: $threadName")
    return "Result Two"
}

private suspend fun taskThree(threadName: String): String {
    println("Task Three start: $threadName")
    delay(2000)
    println("\nTask Three End: $threadName")
    return "Result Three"
}

//If you try to update a UI component from a background thread, the application will crash as it is handled by the main thread
private suspend fun setTextOnMainThread(input: String) {
    withContext(Main) {
        //Code to update the UI component
    }
}