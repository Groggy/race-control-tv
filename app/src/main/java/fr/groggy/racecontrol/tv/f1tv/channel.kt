package fr.groggy.racecontrol.tv.f1tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class F1TvChannelResponse(
    val self: String,
    val name: String,
    @Json(name = "channel_type") val channelType: String,
    @Json(name = "driveroccurrence_urls") val driverOccurrenceUrls: List<String>
)

inline class F1TvChannelId(val value: String)

sealed class F1TvBasicChannelType {
    companion object {
        object Wif : F1TvBasicChannelType()
        object PitLane : F1TvBasicChannelType()
        object Tracker : F1TvBasicChannelType()
        object Data : F1TvBasicChannelType()
        data class Unknown(val type: String, val name: String) : F1TvBasicChannelType()

        fun from(type: String, name: String): F1TvBasicChannelType =
            when(type) {
                "wif" -> Wif
                "other" -> when(name) {
                    "pit lane" -> PitLane
                    "driver" -> Tracker
                    "data" -> Data
                    else -> Unknown(type, name)
                }
                else -> Unknown(type, name)
            }
    }
}

sealed class F1TvChannel {
    abstract val id: F1TvChannelId
}

data class F1TvBasicChannel(
    override val id: F1TvChannelId,
    val type: F1TvBasicChannelType
) : F1TvChannel()

data class F1TvOnboardChannel(
    override val id: F1TvChannelId,
    val name: String,
    val driver: F1TvDriverId
) : F1TvChannel()