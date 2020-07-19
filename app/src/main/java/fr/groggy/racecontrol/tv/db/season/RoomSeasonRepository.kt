package fr.groggy.racecontrol.tv.db.season

import fr.groggy.racecontrol.tv.core.season.SeasonRepository
import fr.groggy.racecontrol.tv.db.IdListMapper
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import fr.groggy.racecontrol.tv.f1tv.F1TvEventId
import fr.groggy.racecontrol.tv.f1tv.F1TvSeason
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import java.time.Year
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomSeasonRepository @Inject constructor(
    database: RaceControlTvDatabase,
    private val eventIdListMapper: IdListMapper<F1TvEventId>
) : SeasonRepository {

    private val dao = database.seasonDao()

    override suspend fun findAll(): Set<F1TvSeason> =
        dao.findAll()
            .map { F1TvSeason(
                id = F1TvSeasonId(it.id),
                name = it.name,
                year = Year.of(it.year),
                events = eventIdListMapper.fromDto(it.events)
            ) }
            .toSet()

    override suspend fun save(set: Set<F1TvSeason>) {
        val entities = set.map { SeasonEntity(
            id = it.id.value,
            name = it.name,
            year = it.year.value,
            events = eventIdListMapper.toDto(it.events)
        ) }
        dao.upsertAll(entities)
    }

}