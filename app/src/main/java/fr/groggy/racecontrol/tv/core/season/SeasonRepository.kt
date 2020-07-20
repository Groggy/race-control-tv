package fr.groggy.racecontrol.tv.core.season

import fr.groggy.racecontrol.tv.f1tv.F1TvSeason
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {

    fun observe(id: F1TvSeasonId): Flow<F1TvSeason?>

    suspend fun save(season: F1TvSeason)

}