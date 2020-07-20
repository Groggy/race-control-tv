package fr.groggy.racecontrol.tv.core.image

import fr.groggy.racecontrol.tv.f1tv.F1TvImage
import fr.groggy.racecontrol.tv.f1tv.F1TvImageId
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun observe(ids: List<F1TvImageId>): Flow<List<F1TvImage>>

    suspend fun save(images: List<F1TvImage>)

}