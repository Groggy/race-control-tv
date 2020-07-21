package fr.groggy.racecontrol.tv.kv.season

import fr.groggy.racecontrol.tv.core.season.CurrentSeasonIdRepository
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import fr.groggy.racecontrol.tv.kv.SharedPreferencesStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesCurrentSeasonIdRepository @Inject constructor(
    private val store: SharedPreferencesStore
) : CurrentSeasonIdRepository {

    companion object {
        private const val KEY = "CURRENT_SEASON_ID"
    }

    override fun observe(): Flow<F1TvSeasonId?> =
        store.observeString(KEY)
            .map { if (it != null) F1TvSeasonId(it) else null } // id?.let { F1TvSeasonId(it) } cause a ClassCastException

    override fun save(id: F1TvSeasonId) =
        store.update {
            putString(KEY, id.value)
        }

}