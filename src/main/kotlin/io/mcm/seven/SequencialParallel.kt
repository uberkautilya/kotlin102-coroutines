package io.mcm.seven

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 *
 */
fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")
    val startTime = System.currentTimeMillis()

    //By default, all child coroutines are started in parallel. job1.join() will ensure the second job is launched only after the first one is complete
    val parentJob = CoroutineScope(Default).launch {

        val job1 = launch { println("{taskOne(Thread.currentThread().name)}: ${taskOne(Thread.currentThread().name)}") }
        job1.join()
        val job2 = launch { println("{taskTwo(Thread.currentThread().name)}: ${taskTwo(Thread.currentThread().name)}") }
        job2.join()
        val job3 = launch { println("{taskThree(Thread.currentThread().name)}: ${taskThree(Thread.currentThread().name)}") }
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
    println("Task One Simulation: $threadName")
    delay(2000)
    println("\nTask One End: $threadName")
    return "Result One"
}

private suspend fun taskTwo(threadName: String): String {
    println("Task Two Simulation: $threadName")
    delay(2000)
    println("\nTask Two End: $threadName")
    return "Result Two"
}

private suspend fun taskThree(threadName: String): String {
    println("Task Three Simulation: $threadName")
    delay(2000)
    println("\nTask Three End: $threadName")
    return "Result Three"
}
