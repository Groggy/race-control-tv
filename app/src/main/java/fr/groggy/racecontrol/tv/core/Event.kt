package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.f1tv.F1TvEventId

data class Event(
    val id: F1TvEventId,
    val name: String,
    val period: LocalDatePeriod,
    val sessions: List<Session>
) {

    companion object : FromState<F1TvEventId, Event> {
        override fun from(id: F1TvEventId, state: State): Event? =
            state.events[id]?.let { Event(
                id = it.id,
                name = it.name,
                period = it.period,
                sessions = Session.from(it.sessions, state).sortedByDescending { session -> session.period.start }
            ) }
    }

}