package fr.groggy.racecontrol.tv.core

interface FromState<I, A: Any> {

    fun from(id: I, state: State): A?

    fun from(ids: List<I>, state: State): List<A> =
        ids.mapNotNull { from(it, state) }

}