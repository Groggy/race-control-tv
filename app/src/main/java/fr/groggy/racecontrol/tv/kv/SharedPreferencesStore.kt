package fr.groggy.racecontrol.tv.kv

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.groggy.racecontrol.tv.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val sharedPreferences by lazy {
        val name = context.getString(R.string.data_key_value_store)
        context.getSharedPreferences(name, MODE_PRIVATE)
    }
    
    fun observeString(key: String): Flow<String?> =
        observe(key) { sharedPreferences, k -> sharedPreferences.getString(k, null) }

    private fun <T> observe(key: String, findValue: (SharedPreferences, String) -> T?): Flow<T?> =
        flow {
            val channel = Channel<T?>(CONFLATED)
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, changedKey ->
                if (changedKey == key) {
                    channel.offer(findValue(sharedPreferences, key))
                }
            }
            channel.offer(findValue(sharedPreferences, key))
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            try {
                for (value in channel) {
                    emit(value)
                }
            } finally {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.distinctUntilChanged()

    fun findString(key: String): String? =
        sharedPreferences.getString(key, null)

    fun update(f: SharedPreferences.Editor.() -> Unit): Unit =
        with(sharedPreferences.edit()) {
            f()
            apply()
        }

}