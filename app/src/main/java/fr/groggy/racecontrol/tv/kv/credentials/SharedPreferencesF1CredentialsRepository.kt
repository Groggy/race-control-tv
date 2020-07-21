package fr.groggy.racecontrol.tv.kv.credentials

import fr.groggy.racecontrol.tv.core.credentials.F1CredentialsRepository
import fr.groggy.racecontrol.tv.f1.F1Credentials
import fr.groggy.racecontrol.tv.kv.SharedPreferencesStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesF1CredentialsRepository @Inject constructor(
    private val store: SharedPreferencesStore
) : F1CredentialsRepository {

    companion object {
        private const val LOGIN_KEY = "F1_LOGIN"
        private const val PASSWORD_KEY = "F1_PASSWORD"
    }

    override fun find(): F1Credentials? {
        val login = store.findString(LOGIN_KEY)
        val password = store.findString(PASSWORD_KEY)
        return if (login != null && password != null) {
            F1Credentials(
                login = login,
                password = password
            )
        } else {
            null
        }
    }

    override fun save(credentials: F1Credentials) =
        store.update {
            putString(LOGIN_KEY, credentials.login)
            putString(PASSWORD_KEY, credentials.password)
        }

    override fun delete() =
        store.update {
            remove(LOGIN_KEY)
            remove(PASSWORD_KEY)
        }
}