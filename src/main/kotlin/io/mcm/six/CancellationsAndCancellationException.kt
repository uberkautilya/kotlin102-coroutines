package io.mcm.six

import kotlinx.coroutines.*

/**
 * For a coroutine to be Cancellable, it needs to be cooperative - from the package kotlinx.coroutines.*
 */
fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")
    val startTime = System.currentTimeMillis()

    val parentJob = CoroutineScope(Dispatchers.Default).launch {

        val jobOne = launch {
            try {
                println(taskOne(Thread.currentThread().name))
            } catch (e: CancellationException) {
                println("Exception caught safely: ${e.message}")
            } finally {
                println("Resources closed safely")
            }
            //This can be omitted in this case
            if (!isActive) {
                //break the coroutine if this job has been cancelled, in long-running computations
                println("Cancel via isActive if block")
                return@launch
            }
        }
        //If the cancel() fails, wait for the coroutine to join
        jobOne.cancel("My message")
        jobOne.join()
        jobOne.cancelAndJoin() //Same as sequential cancel() and join() commands

        val jobTwo = launch {
            println(taskTwo(Thread.currentThread().name))
        }

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
    delay(5000) //Can be cancelled throwing a CancellationException
    println("Work One End: $threadName")
    return "Result One"
}

private suspend fun taskTwo(threadName: String): String {
    println("Work Two Simulation: $threadName")
    delay(2000)
    println("Work Two End: $threadName")
    return "Result Two"
}