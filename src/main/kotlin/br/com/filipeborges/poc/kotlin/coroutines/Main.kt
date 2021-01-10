package br.com.filipeborges.poc.kotlin.coroutines

import br.com.filipeborges.poc.kotlin.coroutines.network.NetworkOperation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicBoolean

class Main

private fun runAsyncCallback() {
    val isFinalized = AtomicBoolean()
    val data = "Hello World".toByteArray(Charset.defaultCharset())

    NetworkOperation.buildDefault().sendDataAsync(data) {
        isFinalized.set(true)
    }

    while(!isFinalized.get()) {
        println("Aynsc operation not completed yet!!!")
        Thread.sleep(300L)
    }
}

private fun runAsyncSuspended() {
    val data = "Hello World".toByteArray(Charset.defaultCharset())
    val job = GlobalScope.launch {
        val isSucessful = NetworkOperation.buildDefault().sendDataSuspended(data)
        if (isSucessful)
            println("==== Data sent with success ====")
        else
            println("==== Data sent with fail =====")
    }

    while (job.isActive) {
        println("Job not completed yet!!!")
        Thread.sleep(300L)
    }
}

fun main() {
//    runAsyncSuspended()
//    runAsyncCallback()
}