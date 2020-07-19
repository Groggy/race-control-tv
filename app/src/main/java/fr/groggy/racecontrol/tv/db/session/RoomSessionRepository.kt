package fr.groggy.racecontrol.tv.db.session

import fr.groggy.racecontrol.tv.core.InstantPeriod
import fr.groggy.racecontrol.tv.core.session.SessionRepository
import fr.groggy.racecontrol.tv.db.IdListMapper
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import fr.groggy.racecontrol.tv.f1tv.F1TvChannelId
import fr.groggy.racecontrol.tv.f1tv.F1TvImageId
import fr.groggy.racecontrol.tv.f1tv.F1TvSession
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionStatus.Companion.Live
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionStatus.Companion.Replay
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionStatus.Companion.Unknown
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionStatus.Companion.Upcoming
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomSessionRepository @Inject constructor(
    database: RaceControlTvDatabase,
    private val channelIdListMapper: IdListMapper<F1TvChannelId>,
    private val imageIdListMapper: IdListMapper<F1TvImageId>
) : SessionRepository {

    companion object {
        private const val REPLAY = "REPLAY"
        private const val LIVE = "LIVE"
        private const val UPCOMING = "UPCOMING"
    }

    private val dao = database.sessionDao()

    override suspend fun findAll(): Set<F1TvSession> =
        dao.findAll()
            .map { F1TvSession(
                id = F1TvSessionId(it.id),
                name = it.name,
                status = when(it.status) {
                    REPLAY -> Replay
                    LIVE -> Live
                    UPCOMING -> Upcoming
                    else -> Unknown(it.status)
                },
                period = InstantPeriod(
                    start = Instant.ofEpochMilli(it.startTime),
                    end = Instant.ofEpochMilli(it.endTime)
                ),
                available = it.available,
                images = imageIdListMapper.fromDto(it.images),
                channels = channelIdListMapper.fromDto(it.channels)
            ) }
            .toSet()

    override suspend fun save(set: Set<F1TvSession>) {
        val entities = set.map { SessionEntity(
            id = it.id.value,
            name = it.name,
            status = when(it.status) {
                Replay -> REPLAY
                Live -> LIVE
                Upcoming -> UPCOMING
                is Unknown -> it.status.value
            },
            startTime = it.period.start.toEpochMilli(),
            endTime = it.period.end.toEpochMilli(),
            available = it.available,
            images = imageIdListMapper.toDto(it.images),
            channels = channelIdListMapper.toDto(it.channels)
        ) }
        dao.upsertAll(entities)
    }

}