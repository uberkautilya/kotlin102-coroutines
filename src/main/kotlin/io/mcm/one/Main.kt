package io.mcm.one

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

/**
 * The application does not wait for the coroutine work to finish by default, unlike thread
 *
 * Coroutine scope can take in other values
 * kotlinx.coroutines.Dispatchers.Main //For Android main thread
 * kotlinx.coroutines.Dispatchers.Default //For heavy work
 * kotlinx.coroutines.Dispatchers.IO //For download from internet etc.
 */

fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")

    //To group a bunch of coroutines together
    CoroutineScope(Default).launch {

        println("Subtask simulation: ${Thread.currentThread().name}")
        Thread.sleep(2000) //Work simulation
        println("Subtask end: ${Thread.currentThread().name}")
    }

    println("Main application end: ${Thread.currentThread().name}")
    Thread.sleep(2500)
    println("Done waiting for coroutines. Exiting application")
}

private fun subtaskWithThread() {
    //Create a new thread, while the main thread continues execution

    thread {
        println("Subtask simulation: ${Thread.currentThread().name}")
        Thread.sleep(2000) //Work simulation
        println("Subtask end: ${Thread.currentThread().name}")
    }

    //Execution of program waits for all threads to complete, if they are not daemon threads
}