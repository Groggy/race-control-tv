package fr.groggy.racecontrol.tv.kv.season

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.groggy.racecontrol.tv.core.season.CurrentSeasonIdRepository
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import fr.groggy.racecontrol.tv.kv.SharedPreferencesSingleValueRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesCurrentSeasonIdRepository @Inject constructor(
    @ApplicationContext context: Context
) : SharedPreferencesSingleValueRepository<F1TvSeasonId>(
    context = context,
    id = "CURRENT_SEASON_ID",
    toDto = { it.value },
    fromDto = { F1TvSeasonId(it) }
), CurrentSeasonIdRepository