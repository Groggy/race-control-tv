package fr.groggy.racecontrol.tv.core.channel

import android.util.Log
import fr.groggy.racecontrol.tv.core.driver.DriverService
import fr.groggy.racecontrol.tv.f1tv.F1TvChannelId
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvOnboardChannel
import fr.groggy.racecontrol.tv.utils.coroutines.concurrentMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelService @Inject constructor(
    private val repository: ChannelRepository,
    private val f1Tv: F1TvClient,
    private val driverService: DriverService
) {

    companion object {
        private val TAG = ChannelService::class.simpleName
    }

    suspend fun loadChannelsWithDrivers(ids: List<F1TvChannelId>) {
        Log.d(TAG, "loadChannelsWithDrivers")
        val channels = ids.concurrentMap { f1Tv.getChannel(it) }
        repository.save(channels)
        channels.forEach { if (it is F1TvOnboardChannel) driverService.loadDriverWithImages(it.driver) }
    }

}