package io.mcm.six

import kotlinx.coroutines.*

/**
 * withTimeout() creates coroutines, which however cannot be cancelled
 */
fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")
    val startTime = System.currentTimeMillis()

    val parentJob = CoroutineScope(Dispatchers.Default).launch {

        //Exception if timeout while not yet completed. Breaks the parent job execution
        withTimeout(2050) {
            println(taskOne(Thread.currentThread().name))
            //This can be omitted in this case
            if (!isActive) {
                //break the coroutine if this job has been cancelled, in long-running computations
                println("Cancel via isActive if block")
            }
        }

        //withTimeoutOrNull() will not throw exception if the job is not completed within the duration
        val wTOrNull = withTimeoutOrNull(2000) {
            println(taskTwo(Thread.currentThread().name))
            "Completed"
        }
        println("wTOrNull: $wTOrNull")

    }
    parentJob.invokeOnCompletion {
        it?.let {
            println("Parent job failed: ${it.message}")
        } ?: println("Parent and child jobs finished")
    }

    //join function can also be called on a deferred object, as it inherits from Job
    runBlocking { parentJob.join() }
    println("Main application end: ${Thread.currentThread().name}")
    println("Time of execution: ${System.currentTimeMillis() - startTime}")
}

//-------------------------------------------------------------------------------------

private suspend fun taskOne(threadName: String): String {
    println("Work One Simulation: $threadName")
    //Thread.sleep(2000) cannot be cancelled - not cooperative concurrency
    delay(2000) //Can be cancelled throwing a CancellationException
    println("Work One End: $threadName")
    return "Result One"
}

private suspend fun taskTwo(threadName: String): String {
    println("Work Two Simulation: $threadName")
    delay(2000)
    println("Work Two End: $threadName")
    return "Result Two"
}