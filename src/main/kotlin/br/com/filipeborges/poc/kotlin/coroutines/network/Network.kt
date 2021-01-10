package br.com.filipeborges.poc.kotlin.coroutines.network

import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

abstract class NetworkOperation {
    abstract fun sendDataAsync(data: ByteArray, finalizedCallback: (isSuccess: Boolean) -> Unit)

    abstract suspend fun sendDataSuspended(data: ByteArray): Boolean

    companion object {
        fun buildDefault(): NetworkOperation = MockNetworkOperation()
    }
}

class MockNetworkOperation : NetworkOperation() {
    override fun sendDataAsync(data: ByteArray, finalizedCallback: (isSuccess: Boolean) -> Unit) {
        send(data).whenComplete { _, t ->
            if (t == null)
                finalizedCallback(true)
            else
                finalizedCallback(false)
        }
    }

    override suspend fun sendDataSuspended(data: ByteArray): Boolean = suspendCoroutine {
        send(data).whenComplete { _, t ->
            if (t == null)
                it.resume(true)
            else
                it.resumeWithException(t)
        }
    }

    private fun send(data: ByteArray) = CompletableFuture.runAsync {
        data.forEach {
            println("======= Mock - Sending data: $it =======")
            Thread.sleep(1000L)
            println("======= Mock - Data sent ========")
        }
    }
}