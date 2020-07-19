package fr.groggy.racecontrol.tv.kv

import android.content.Context
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.core.SingleValueRepository

open class SharedPreferencesSingleValueRepository<T> (
    private val context: Context,
    private val id: String,
    private val toDto: (T) -> String,
    private val fromDto: (String) -> T
) : SingleValueRepository<T> {

    private val sharedPreferences by lazy {
        val name = context.getString(R.string.data_key_value_store)
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    override fun find(): T? =
        sharedPreferences.getString(id, null)?.let { fromDto(it) }

    override fun save(value: T) {
        with(sharedPreferences.edit()) {
            putString(id, toDto(value))
            commit()
        }
    }

}