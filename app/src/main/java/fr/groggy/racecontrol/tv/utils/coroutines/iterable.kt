package fr.groggy.racecontrol.tv.utils.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll

suspend fun <A> Iterable<A>.concurrentForEach(f: suspend (A) -> Unit): Unit = coroutineScope {
    map { async { f(it) } }.joinAll()
}

suspend fun <A, B> Iterable<A>.concurrentMap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}