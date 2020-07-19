package fr.groggy.racecontrol.tv.kv.token

import android.content.Context
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.groggy.racecontrol.tv.core.token.F1TokenRepository
import fr.groggy.racecontrol.tv.f1.F1Token
import fr.groggy.racecontrol.tv.kv.SharedPreferencesSingleValueRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesF1TokenRepository @Inject constructor(
    @ApplicationContext context: Context
) : SharedPreferencesSingleValueRepository<F1Token>(
    context = context,
    id = "F1_TOKEN",
    toDto = { it.value.toString() },
    fromDto = { F1Token(JWT(it)) }
), F1TokenRepository