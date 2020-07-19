package fr.groggy.racecontrol.tv.utils.http

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSource
import java.io.IOException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val JSON = "application/json; charset=utf-8".toMediaType()

fun <T> T.toJsonRequestBody(adapter: JsonAdapter<T>): RequestBody =
    adapter.toJson(this).toRequestBody(JSON)

fun <T> Response.parseJsonBody(adapter: JsonAdapter<T>): T =
    if (isSuccessful) body?.source()?.parseJson(adapter)!!
    else throw IllegalStateException("HTTP request failed with status $code")

fun <T> BufferedSource.parseJson(adapter: JsonAdapter<T>): T? =
    adapter.fromJson(this)