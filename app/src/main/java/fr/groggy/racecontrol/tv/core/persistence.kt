package fr.groggy.racecontrol.tv.core

import android.util.Log
import arrow.core.getOrElse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.rx3.rxCompletable

interface SingleValueRepository<T> {

    fun find(): T?

    fun save(value: T)

}

inline fun <reified T> UpdatableStore<T>.persistChanges(repository: SingleValueRepository<T>, tag: String? = null): Disposable =
    observeNullable()
        .skip(1) // Skipping hydration update
        .flatMap { it.map { v -> Observable.just(v) }.getOrElse { Observable.empty() } }
        .subscribe {
            repository.save(it)
            Log.d(tag, "Persisted ${T::class.simpleName}")
        }

inline fun <reified T> UpdatableStore<T>.hydrate(repository: SingleValueRepository<T>, tag: String? = null) {
    val value = repository.find()
    value?.let { set(it) }
    Log.d(tag, "${if (value == null) 0 else 1} ${T::class.simpleName} hydrated")
}

interface MultiValuesRepository<T> {

    suspend fun findAll(): Set<T>

    suspend fun save(set: Set<T>)

}

inline fun <reified V> UpdatableStore<out Map<*, V>>.persistChanges(repository: MultiValuesRepository<V>, tag: String? = null): Disposable =
    observe()
        .map { it.values.toSet() }
        .scan<Pair<Set<V>?, Set<V>>>(null to emptySet()) { (old, _), new ->
            if (old == null) new to emptySet() // Skipping hydration update
            else new to new.subtract(old)
        }
        .map { (_, createdOrUpdated) -> createdOrUpdated }
        .filter { it.isNotEmpty() }
        .flatMapCompletable { createdOrUpdated -> rxCompletable {
            repository.save(createdOrUpdated)
            Log.d(tag, "Persisted ${createdOrUpdated.size} ${V::class.simpleName}")
        } }
        .subscribe()

suspend inline fun <K, reified V> UpdatableStore<Map<K, V>>.hydrate(repository: MultiValuesRepository<V>, tag: String? = null, key: (V) -> K) {
    val values = repository.findAll().associateBy(key)
    set(values)
    Log.d(tag, "${values.size} ${V::class.simpleName} hydrated")
}