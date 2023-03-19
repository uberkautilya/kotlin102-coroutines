package io.mcm.seven

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default

/**
 * CoroutineScope(Default).launch(handler) {} -> exception will be handled gracefully, exiting the parent coroutine
 * Alternately if a CancellationException is thrown, it will not kill sibling coroutines, or its parent
 * Contain child within SupervisorScope {} within parent to continue other child coroutines of the parent
 */
val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    println("Coroutine Exception handler: ${exception.message}")
}


fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")
    val startTime = System.currentTimeMillis()

    //Passing the coroutineExceptionHandler will handle the exception thrown by any child gracefully
    val parentJob = CoroutineScope(Default).launch(coroutineExceptionHandler) {

        //Will not affect the parent as this throws a CancellationException
        launch { println("{taskTwo(Thread.currentThread().name)}: ${taskTwo(Thread.currentThread().name)}") }

        //Will continue with other children if one throws an exception
        supervisorScope {
            launch { println("{taskOne(Thread.currentThread().name)}: ${taskOne(Thread.currentThread().name)}") }
            launch { println("{taskThree(Thread.currentThread().name)}: ${taskThree(Thread.currentThread().name)}") }
        }
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
    println("Work One Simulation: $threadName")
    throw Exception("Exception in taskOne")
}

private suspend fun taskTwo(threadName: String): String {
    println("Work Two Simulation: $threadName")
    throw CancellationException("Cancel taskTwo")
}

private suspend fun taskThree(threadName: String): String {
    println("Work Three Simulation: $threadName")
    delay(3000) //Can be cancelled throwing a CancellationException
    println("Work Three End: $threadName")
    return "Result Three"
}
