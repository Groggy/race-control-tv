package fr.groggy.racecontrol.tv.kv.token

import com.auth0.android.jwt.JWT
import fr.groggy.racecontrol.tv.core.token.F1TokenRepository
import fr.groggy.racecontrol.tv.f1.F1Token
import fr.groggy.racecontrol.tv.kv.SharedPreferencesStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesF1TokenRepository @Inject constructor(
    private val store: SharedPreferencesStore
) : F1TokenRepository {

    companion object {
        private const val KEY = "F1_TOKEN"
    }

    override fun find(): F1Token? =
        store.findString(KEY)?.let { F1Token(JWT(it)) }

    override fun save(token: F1Token) =
        store.putString(KEY, token.value.toString())

}