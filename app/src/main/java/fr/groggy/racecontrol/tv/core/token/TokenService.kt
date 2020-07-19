package fr.groggy.racecontrol.tv.core.token

import android.content.Context
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.core.*
import fr.groggy.racecontrol.tv.f1.F1Client
import fr.groggy.racecontrol.tv.f1.F1Token
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvToken
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenService @Inject constructor(
    store: UpdatableStore<State>,
    private val f1TokenRepository: F1TokenRepository,
    private val f1TvTokenRepository: F1TvTokenRepository,
    private val f1Client: F1Client,
    private val f1TvClient: F1TvClient,
    @ApplicationContext private val context: Context
) : Hydratable {

    companion object {
        private val TAG = TokenService::class.simpleName
        private val JWT_LEEWAY = Duration.ofMinutes(1)
    }

    private val f1TokenStore = store.lens(State.f1Token)
    private val f1TvTokenStore = store.lens(State.f1TvToken)

    override suspend fun hydrate() {
        f1TokenStore.hydrate(f1TokenRepository, TAG)
        f1TvTokenStore.hydrate(f1TvTokenRepository, TAG)
        f1TokenStore.persistChanges(f1TokenRepository, TAG)
        f1TvTokenStore.persistChanges(f1TvTokenRepository, TAG)
    }

    suspend fun loadAndGetF1Token(): F1Token =
        loadAndGetToken(f1TokenStore, { it.value }) {
            val login = context.resources.getString(R.string.f1_login)
            val password = context.resources.getString(R.string.f1_password)
            f1Client.authenticate(login, password)
        }

    suspend fun loadAndGetF1TvToken(): F1TvToken =
        loadAndGetToken(f1TvTokenStore, { it.value }) {
            val f1Token = loadAndGetF1Token()
            f1TvClient.authenticate(f1Token)
        }

    private suspend fun <T> loadAndGetToken(store: UpdatableStore<T>, jwt: (T) -> JWT, fetch: suspend () -> T): T {
        val existingToken = store.get()
        return if (existingToken == null || jwt(existingToken).isExpired(JWT_LEEWAY.seconds)) {
            val token = fetch()
            store.set(token)
            token
        } else {
            existingToken
        }
    }

}