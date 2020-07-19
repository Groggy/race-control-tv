package fr.groggy.racecontrol.tv.db.event

import fr.groggy.racecontrol.tv.core.LocalDatePeriod
import fr.groggy.racecontrol.tv.core.event.EventRepository
import fr.groggy.racecontrol.tv.db.IdListMapper
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import fr.groggy.racecontrol.tv.f1tv.F1TvEvent
import fr.groggy.racecontrol.tv.f1tv.F1TvEventId
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomEventRepository @Inject constructor(
    database: RaceControlTvDatabase,
    private val sessionIdListMapper: IdListMapper<F1TvSessionId>
) : EventRepository {

    private val dao = database.eventDao()

    override suspend fun findAll(): Set<F1TvEvent> =
        dao.findAll()
            .map { F1TvEvent(
                id = F1TvEventId(it.id),
                name = it.name,
                period = LocalDatePeriod(
                    start = LocalDate.ofEpochDay(it.startDate),
                    end = LocalDate.ofEpochDay(it.endDate)
                ),
                sessions = sessionIdListMapper.fromDto(it.sessions)
            ) }
            .toSet()

    override suspend fun save(set: Set<F1TvEvent>) {
        val entities = set.map { EventEntity(
            id = it.id.value,
            name = it.name,
            startDate = it.period.start.toEpochDay(),
            endDate = it.period.end.toEpochDay(),
            sessions = sessionIdListMapper.toDto(it.sessions)
        ) }
        dao.upsertAll(entities)
    }

}