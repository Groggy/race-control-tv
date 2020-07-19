package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.concurrentMap
import fr.groggy.racecontrol.tv.f1tv.F1TvChannelId
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvOnboardChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelService @Inject constructor(
    store: UpdatableStore<State>,
    private val f1TvClient: F1TvClient,
    private val driverService: DriverService
) {

    private val channelsStore = store.lens(State.channels)

    suspend fun loadChannelsWithDrivers(ids: List<F1TvChannelId>) {
        val channels = ids.concurrentMap { f1TvClient.getChannel(it) }
        channelsStore.modify { it + channels.associateBy { channel -> channel.id } }
        channels.forEach { if (it is F1TvOnboardChannel) driverService.loadDriverWithImages(it.driver) }
    }

    suspend fun loadChannel(id: F1TvChannelId) {
        val channel = f1TvClient.getChannel(id)
        channelsStore.modify { it + (channel.id to channel) }
    }

}