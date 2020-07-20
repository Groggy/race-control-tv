package fr.groggy.racecontrol.tv.utils.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.joinAll

suspend fun <A> Iterable<A>.concurrentForEach(f: suspend (A) -> Unit): Unit = coroutineScope {
    map { async { f(it) } }.joinAll()
}

suspend fun <A, B> Iterable<A>.concurrentMap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

fun <A, B> List<A>.traverse(f: (A) -> Flow<B>): Flow<List<B>> =
    map(f).fold(flowOf(emptyList())) { accFlow, itemFlow ->
        accFlow.combine(itemFlow) { acc, item -> acc + item }
    }