package fr.groggy.racecontrol.tv.f1tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class F1TvDriverResponse(
    val self: String,
    val name: String,
    @Json(name = "driver_tla") val driverTla: String,
    @Json(name = "driver_racingnumber") val driverRacingNumber: Int,
    @Json(name = "image_urls") val imageUrls: List<String>
)

inline class F1TvDriverId(val value: String)

data class F1TvDriver(
    val id: F1TvDriverId,
    val name: String,
    val shortName: String,
    val racingNumber: Int,
    val images: List<F1TvImageId>
)