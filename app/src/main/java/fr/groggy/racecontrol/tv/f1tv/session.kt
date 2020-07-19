package fr.groggy.racecontrol.tv.f1tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import fr.groggy.racecontrol.tv.core.InstantPeriod
import java.time.Clock
import java.time.Instant

@JsonClass(generateAdapter = true)
data class F1TVSessionAvailabilityDetailsResponse(
    @Json(name = "is_available") val isAvailable: Boolean
)

@JsonClass(generateAdapter = true)
data class F1TvSessionResponse(
    val self: String,
    val name: String,
    val status: String,
    @Json(name = "image_urls") val imageUrls: List<String>,
    @Json(name = "channel_urls") val channelUrls: List<String>,
    @Json(name = "start_time") val startTime: String,
    @Json(name = "end_time") val endTime: String,
    @Json(name = "availability_details") val availabilityDetails: F1TVSessionAvailabilityDetailsResponse
)

inline class F1TvSessionId(val value: String)

sealed class F1TvSessionStatus {
    companion object {
        object Replay : F1TvSessionStatus()
        object Live : F1TvSessionStatus()
        object Upcoming : F1TvSessionStatus()
        data class Unknown(val value: String) : F1TvSessionStatus()

        fun from(value: String): F1TvSessionStatus =
            when(value) {
                "replay" -> Replay
                "live" -> Live
                "upcoming" -> Upcoming
                else -> Unknown(value)
            }
    }
}

data class F1TvSession(
    val id: F1TvSessionId,
    val name: String,
    val status: F1TvSessionStatus,
    val period: InstantPeriod,
    val available: Boolean,
    val images: List<F1TvImageId>,
    val channels: List<F1TvChannelId>
) {

    fun isFutureSession(clock: Clock): Boolean = period.start.isAfter(Instant.now(clock))

}