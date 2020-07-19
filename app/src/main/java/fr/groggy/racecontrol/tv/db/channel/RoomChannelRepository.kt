package fr.groggy.racecontrol.tv.db.channel

import fr.groggy.racecontrol.tv.core.channel.ChannelRepository
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import fr.groggy.racecontrol.tv.f1tv.*
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.Data
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.PitLane
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.Tracker
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.Unknown
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.Wif
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomChannelRepository @Inject constructor(
    database: RaceControlTvDatabase
) : ChannelRepository {

    companion object {
        private const val WIF = "WIF"
        private const val PIT_LANE = "PIT_LANE"
        private const val TRACKER = "TRACKER"
        private const val DATA = "DATA"
        private const val ONBOARD = "ONBOARD"
    }

    private val dao = database.channelDao()

    override suspend fun findAll(): Set<F1TvChannel> =
        dao.findAll()
            .map {
                val id = F1TvChannelId(it.id)
                if (it.type == ONBOARD) F1TvOnboardChannel(
                    id = id,
                    name = it.name!!,
                    driver = F1TvDriverId(it.driver!!)
                ) else F1TvBasicChannel(
                    id = id,
                    type = when(it.type) {
                        WIF -> Wif
                        PIT_LANE -> PitLane
                        TRACKER -> Tracker
                        DATA -> Data
                        else -> Unknown(it.type, it.name!!)
                    }
                )
            }
            .toSet()

    override suspend fun save(set: Set<F1TvChannel>) {
        val entities = set.map {
            when(it) {
                is F1TvBasicChannel -> {
                    val (type, name) = when(it.type) {
                        Wif -> WIF to null
                        PitLane -> PIT_LANE to null
                        Tracker -> TRACKER to null
                        Data -> DATA to null
                        is Unknown -> it.type.type to it.type.name
                    }
                    ChannelEntity(
                        id = it.id.value,
                        type = type,
                        name = name,
                        driver = null
                    )
                }
                is F1TvOnboardChannel -> ChannelEntity(
                    id = it.id.value,
                    type = ONBOARD,
                    name = it.name,
                    driver = it.driver.value
                )
            }
        }
        dao.upsertAll(entities)
    }

}