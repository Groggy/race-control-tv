package fr.groggy.racecontrol.tv.core.season

import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import kotlinx.coroutines.flow.Flow

interface CurrentSeasonIdRepository {

    fun observe(): Flow<F1TvSeasonId?>

    fun save(id: F1TvSeasonId)

}