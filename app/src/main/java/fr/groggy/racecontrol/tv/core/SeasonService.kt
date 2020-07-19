package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId.Companion.CURRENT
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonService @Inject constructor(
    store: UpdatableStore<State>,
    private val f1TvClient: F1TvClient,
    private val eventService: EventService
) {

    private val currentSeasonIdStore = store.lens(State.currentSeasonId)
    private val seasonsStore = store.lens(State.seasons)

    suspend fun loadCurrentSeason() {
        val season = f1TvClient.getSeason(CURRENT)
        seasonsStore.modify { it + (season.id to season) }
        currentSeasonIdStore.set(season.id)
        eventService.loadEventsWithAvailableSessions(season.events)
    }

}