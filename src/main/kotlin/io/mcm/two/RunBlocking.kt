package io.mcm.two

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main(args: Array<String>) { //executed on the main thread

    println("Main application: ${Thread.currentThread().name}")

    //To group a bunch of coroutines together
    val parentJob = CoroutineScope(Default).launch {
        //Execution of line of code needn't be on a single Thread

        println("Subtask simulation: ${Thread.currentThread().name}")
        //Work simulation: Pause this particular coroutine, not the entire thread: Other coroutines continue execution
        delay(5000)
        println("Subtask end: ${Thread.currentThread().name}")
    }

    println("Main application end: ${Thread.currentThread().name}")

    runBlocking {

        //Called only from a coroutine or a suspending function
        parentJob.join()
    }
}
