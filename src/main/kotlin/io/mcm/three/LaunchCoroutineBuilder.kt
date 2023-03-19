package io.mcm.three

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Coroutine Builder functions: launch, async and runBlocking
 *
 */

fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")

    val startTime = System.currentTimeMillis()
    val parentJob = CoroutineScope(Default).launch {

        //Both the below coroutines are non-blocking
        val job1 = launch {
            val dataOneResult = getDataOne(Thread.currentThread().name)
            println("dataOneResult: $dataOneResult")
        }
        job1.join() //Wait for job1 to finish, before the next lines are executed

        val job2 = launch {
            val dataTwoResult = getDataTwo(Thread.currentThread().name)
            println("dataTwoResult: $dataTwoResult")
        }

    }

    println("Main application end: ${Thread.currentThread().name}")
    runBlocking { parentJob.join() }
    println("Time of execution: ${System.currentTimeMillis() - startTime}")
}


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