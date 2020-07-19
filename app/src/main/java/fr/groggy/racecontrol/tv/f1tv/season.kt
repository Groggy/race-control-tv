package fr.groggy.racecontrol.tv.f1tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Year

@JsonClass(generateAdapter = true)
data class F1TvSeasonResponse(
    val self: String,
    val name: String,
    val year: Int,
    @Json(name = "eventoccurrence_urls") val eventOccurrenceUrls: List<String>
)

inline class F1TvSeasonId(val value: String) {
    companion object {
        val CURRENT = F1TvSeasonId("/api/race-season/current/")
    }
}

data class F1TvSeason(
    val id: F1TvSeasonId,
    val name: String,
    val year: Year,
    val events: List<F1TvEventId>
)