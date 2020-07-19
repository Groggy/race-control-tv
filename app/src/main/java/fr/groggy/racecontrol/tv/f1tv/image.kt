package fr.groggy.racecontrol.tv.f1tv

import android.net.Uri
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class F1TvImageResponse(
    val self: String,
    val url: String,
    val type: String
)

inline class F1TvImageId(val value: String)

sealed class F1TvImageType {
    companion object {
        object Thumbnail : F1TvImageType()
        object Car : F1TvImageType()
        object Headshot : F1TvImageType()

        data class Unknown(val value: String) : F1TvImageType()

        fun from(value: String): F1TvImageType =
            when(value) {
                "Thumbnail" -> Thumbnail
                "Car" -> Car
                "Headshot" -> Headshot
                else -> Unknown(value)
            }
    }
}

data class F1TvImage(
    val id: F1TvImageId,
    val url: Uri,
    val type: F1TvImageType
)