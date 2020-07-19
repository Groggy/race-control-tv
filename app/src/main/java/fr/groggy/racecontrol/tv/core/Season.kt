package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import java.time.Year

data class Season(
    val id: F1TvSeasonId,
    val name: String,
    val year: Year,
    val events: List<Event>
) {

    companion object : FromState<F1TvSeasonId, Season> {
        override fun from(id: F1TvSeasonId, state: State): Season? =
            state.seasons[id]?.let { Season(
                id = it.id,
                name = it.name,
                year = it.year,
                events = Event.from(it.events, state).sortedByDescending { event -> event.period.start }
            ) }
    }

}