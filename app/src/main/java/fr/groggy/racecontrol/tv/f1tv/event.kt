package fr.groggy.racecontrol.tv.f1tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import fr.groggy.racecontrol.tv.core.LocalDatePeriod
import java.time.Clock
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class F1TvEventResponse(
    val self: String,
    val name: String,
    @Json(name = "start_date") val startDate: String,
    @Json(name = "end_date") val endDate: String,
    @Json(name = "sessionoccurrence_urls") val sessionOcurrenceUrls: List<String>
)

inline class F1TvEventId(val value: String)

data class F1TvEvent(
    val id: F1TvEventId,
    val name: String,
    val period: LocalDatePeriod,
    val sessions: List<F1TvSessionId>
) {

    fun isFutureEvent(clock: Clock): Boolean = period.start.isAfter(LocalDate.now(clock))

}