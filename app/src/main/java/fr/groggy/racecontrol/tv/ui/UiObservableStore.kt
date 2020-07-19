package fr.groggy.racecontrol.tv.ui

import fr.groggy.racecontrol.tv.core.State
import fr.groggy.racecontrol.tv.core.Store
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class UiObservableStore @Inject constructor(
    private val store: Store
) {

    private val disposables: MutableList<Disposable> = mutableListOf()

    fun <T> observe(selector: (State) -> T?): Observable<T> =
        store.observe(selector)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { disposables.add(it) }

    fun dispose() {
        while (disposables.isNotEmpty()) {
            disposables.removeAt(0).dispose()
        }
    }

}