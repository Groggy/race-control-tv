package fr.groggy.racecontrol.tv.kv.token

import com.auth0.android.jwt.JWT
import fr.groggy.racecontrol.tv.core.token.F1TvTokenRepository
import fr.groggy.racecontrol.tv.f1tv.F1TvToken
import fr.groggy.racecontrol.tv.kv.SharedPreferencesStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesF1TvTokenRepository @Inject constructor(
    private val store: SharedPreferencesStore
) : F1TvTokenRepository {

    companion object {
        private const val KEY = "F1_TV_TOKEN"
    }

    override fun find(): F1TvToken? =
        store.findString(KEY)?.let { F1TvToken(JWT(it)) }

    override fun save(token: F1TvToken) =
        store.update {
            putString(KEY, token.value.toString())
        }

}