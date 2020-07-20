package fr.groggy.racecontrol.tv.db.season

import fr.groggy.racecontrol.tv.core.season.SeasonRepository
import fr.groggy.racecontrol.tv.db.IdListMapper
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import fr.groggy.racecontrol.tv.f1tv.F1TvEventId
import fr.groggy.racecontrol.tv.f1tv.F1TvSeason
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.Year
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomSeasonRepository @Inject constructor(
    database: RaceControlTvDatabase,
    private val eventIdListMapper: IdListMapper<F1TvEventId>
) : SeasonRepository {

    private val dao = database.seasonDao()

    override fun observe(id: F1TvSeasonId): Flow<F1TvSeason?> =
        dao.observeById(id.value)
            .map { season -> season?.let { toSeason(it) } }
            .distinctUntilChanged()

    private fun toSeason(season: SeasonEntity): F1TvSeason =
        F1TvSeason(
            id = F1TvSeasonId(season.id),
            name = season.name,
            year = Year.of(season.year),
            events = eventIdListMapper.fromDto(season.events)
        )

    override suspend fun save(season: F1TvSeason) {
        val entity = toEntity(season)
        dao.upsert(entity)
    }

    private fun toEntity(season: F1TvSeason): SeasonEntity =
        SeasonEntity(
            id = season.id.value,
            name = season.name,
            year = season.year.value,
            events = eventIdListMapper.toDto(season.events)
        )

}