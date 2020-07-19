package fr.groggy.racecontrol.tv.core

import android.util.Log
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.optics.Lens
import arrow.optics.Optional
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.rx3.await
import javax.inject.Inject
import javax.inject.Singleton

typealias Update = (State) -> State

@Singleton
class Store @Inject constructor() {

    companion object {
        private val TAG = Store::class.simpleName
    }

    private val updates: Observer<Update>
    private val state: Observable<State>

    init {
        val initialState = State()
        val updatesSubject = PublishSubject.create<Update>().toSerialized()
        val stateSubject = BehaviorSubject.createDefault(initialState)
        updatesSubject.scan(initialState) { state, update -> update(state) }
            .doOnNext { Log.d(TAG, "Updated state : $it") }
            .subscribeOn(Schedulers.newThread())
            .subscribe(stateSubject)
        updates = updatesSubject
        state = stateSubject
    }

    fun update(f: Update): Unit =
        updates.onNext(f)

    fun <T> observe(selector: (State) -> T?): Observable<T> =
        observeNullable(selector)
            .flatMap { value -> value
                .map { Observable.just(it) }
                .getOrElse { Observable.empty() }
            }
            .distinctUntilChanged()

    fun <T> observeNullable(selector: (State) -> T?): Observable<Option<T>> =
        state
            .map { selector(it).toOption() }
            .distinctUntilChanged()

}

class UpdatableStore<T>(
    private val store: Store,
    private val lens: Optional<State, T>
) {

    fun set(t: T): Unit =
        store.update { lens.set(it, t) }

    fun modify(f: (T) -> T): Unit =
        store.update { lens.modify(it, f) }

    fun <U> lens(lens: Lens<T, U>): UpdatableStore<U> =
        UpdatableStore(store, this.lens.compose(lens))

    fun <U> lens(lens: Optional<T, U>): UpdatableStore<U> =
        UpdatableStore(store, this.lens.compose(lens))

    suspend fun get(): T? =
        store.observeNullable { lens.getOption(it).orNull() }.firstOrError().await().orNull()

}