package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.concurrentMap
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionService @Inject constructor(
    store: UpdatableStore<State>,
    private val f1TvClient: F1TvClient,
    private val channelService: ChannelService,
    private val imageService: ImageService
) {

    private val sessionsStore = store.lens(State.sessions)

    suspend fun loadAvailableSessionsWithImages(ids: List<F1TvSessionId>) {
        val sessions = ids
            .concurrentMap { f1TvClient.getSession(it) }
            .filter { it.available }
            .sortedByDescending { it.period.start }
        sessionsStore.modify { it + sessions.associateBy { session -> session.id } }
        sessions.forEach { imageService.loadImages(it.images) }
    }

    suspend fun loadSessionWithImagesAndChannels(id: F1TvSessionId) {
        val session = f1TvClient.getSession(id)
        sessionsStore.modify { it + (session.id to session) }
        imageService.loadImages(session.images)
        channelService.loadChannelsWithDrivers(session.channels)
    }

}