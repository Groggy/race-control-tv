package fr.groggy.racecontrol.tv.core.season

import fr.groggy.racecontrol.tv.core.*
import fr.groggy.racecontrol.tv.core.event.EventService
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId.Companion.CURRENT
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonService @Inject constructor(
    store: UpdatableStore<State>,
    private val currentSeasonIdRepository: CurrentSeasonIdRepository,
    private val seasonRepository: SeasonRepository,
    private val f1TvClient: F1TvClient,
    private val eventService: EventService
) : Hydratable {

    companion object {
        private val TAG = SeasonService::class.simpleName
    }

    private val currentSeasonIdStore = store.lens(State.currentSeasonId)
    private val seasonsStore = store.lens(State.seasons)

    override suspend fun hydrate() {
        currentSeasonIdStore.hydrate(currentSeasonIdRepository, TAG)
        seasonsStore.hydrate(seasonRepository, TAG) { it.id }
        currentSeasonIdStore.persistChanges(currentSeasonIdRepository, TAG)
        seasonsStore.persistChanges(seasonRepository, TAG)
    }

    suspend fun loadCurrentSeason() {
        val season = f1TvClient.getSeason(CURRENT)
        seasonsStore.modify { it + (season.id to season) }
        currentSeasonIdStore.set(season.id)
        eventService.loadEventsWithAvailableSessions(season.events)
    }

}