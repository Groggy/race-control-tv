package fr.groggy.racecontrol.tv.db

class IdListMapper<T>(
    private val value: (T) -> String,
    private val factory: (String) -> T
) {

    companion object {
        private const val DELIMITER = ","
    }

    fun toDto(ids: List<T>): String =
        ids.joinToString(separator = DELIMITER, transform = value)

    fun fromDto(ids: String): List<T> =
        ids.split(DELIMITER).map(factory)

}