package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.concurrentMap
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvEventId
import java.time.Clock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventService @Inject constructor(
    store: UpdatableStore<State>,
    private val f1TvClient: F1TvClient,
    private val sessionService: SessionService,
    private val clock: Clock
) {

    private val eventsStore = store.lens(State.events)

    suspend fun loadEventsWithAvailableSessions(ids: List<F1TvEventId>) {
        val events = ids
            .concurrentMap { f1TvClient.getEvent(it) }
            .filterNot { it.isFutureEvent(clock) }
            .sortedByDescending { it.period.start }
        eventsStore.modify { it + events.associateBy { event -> event.id } }
        events.forEach { sessionService.loadAvailableSessionsWithImages(it.sessions) }
    }

}