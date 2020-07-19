package fr.groggy.racecontrol.tv.core.session

import fr.groggy.racecontrol.tv.core.*
import fr.groggy.racecontrol.tv.core.channel.ChannelService
import fr.groggy.racecontrol.tv.core.image.ImageService
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId
import fr.groggy.racecontrol.tv.utils.coroutines.concurrentMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionService @Inject constructor(
    store: UpdatableStore<State>,
    private val repository: SessionRepository,
    private val f1TvClient: F1TvClient,
    private val channelService: ChannelService,
    private val imageService: ImageService
) : Hydratable {

    companion object {
        private val TAG = SessionService::class.simpleName
    }

    private val sessionsStore = store.lens(State.sessions)

    override suspend fun hydrate() {
        sessionsStore.hydrate(repository, TAG) { it.id }
        sessionsStore.persistChanges(repository, TAG)
    }

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