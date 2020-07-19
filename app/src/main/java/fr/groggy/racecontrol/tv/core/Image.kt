package fr.groggy.racecontrol.tv.core

import android.net.Uri
import fr.groggy.racecontrol.tv.f1tv.F1TvImageId
import fr.groggy.racecontrol.tv.f1tv.F1TvImageType

data class Image(
    val id: F1TvImageId,
    val url: Uri,
    val type: F1TvImageType
) {

    companion object : FromState<F1TvImageId, Image> {
        override fun from(id: F1TvImageId, state: State): Image? =
            state.images[id]?.let { Image(
                id = it.id,
                url = it.url,
                type = it.type
            ) }
    }

}