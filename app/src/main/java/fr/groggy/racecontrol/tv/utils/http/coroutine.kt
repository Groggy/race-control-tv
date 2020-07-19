package fr.groggy.racecontrol.tv.utils.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CoroutineCallback(private val continuation: Continuation<Response>) : Callback {
    override fun onFailure(call: Call, e: IOException) = continuation.resumeWithException(e)
    override fun onResponse(call: Call, response: Response) = continuation.resume(response)
}

suspend fun Request.execute(httpClient: OkHttpClient): Response {
    val call = httpClient.newCall(this)
    return withContext(Dispatchers.IO) {
        suspendCoroutine { continuation: Continuation<Response> ->
            call.enqueue(CoroutineCallback(continuation))
        }
    }
}