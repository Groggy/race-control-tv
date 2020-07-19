package fr.groggy.racecontrol.tv.kv.token

import android.content.Context
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.groggy.racecontrol.tv.core.token.F1TvTokenRepository
import fr.groggy.racecontrol.tv.f1tv.F1TvToken
import fr.groggy.racecontrol.tv.kv.SharedPreferencesSingleValueRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesF1TvTokenRepository @Inject constructor(
    @ApplicationContext context: Context
) : SharedPreferencesSingleValueRepository<F1TvToken>(
    context = context,
    id = "F1_TV_TOKEN",
    toDto = { it.value.toString() },
    fromDto = { F1TvToken(JWT(it)) }
), F1TvTokenRepository