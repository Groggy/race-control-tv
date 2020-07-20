package fr.groggy.racecontrol.tv.db.image

import android.net.Uri
import fr.groggy.racecontrol.tv.core.image.ImageRepository
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import fr.groggy.racecontrol.tv.f1tv.F1TvImage
import fr.groggy.racecontrol.tv.f1tv.F1TvImageId
import fr.groggy.racecontrol.tv.f1tv.F1TvImageType.Companion.Car
import fr.groggy.racecontrol.tv.f1tv.F1TvImageType.Companion.Headshot
import fr.groggy.racecontrol.tv.f1tv.F1TvImageType.Companion.Thumbnail
import fr.groggy.racecontrol.tv.f1tv.F1TvImageType.Companion.Unknown
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomImageRepository @Inject constructor(
    database: RaceControlTvDatabase
) : ImageRepository {

    companion object {
        private const val THUMBNAIL = "THUMBNAIL"
        private const val CAR = "CAR"
        private const val HEADSHOT = "HEADSHOT"
    }

    private val dao = database.imageDao()

    override fun observe(ids: List<F1TvImageId>): Flow<List<F1TvImage>> =
        dao.observeById(ids.map { it.value })
            .map { images -> images.map { toImage(it) } }
            .distinctUntilChanged()

    private fun toImage(image: ImageEntity): F1TvImage =
        F1TvImage(
            id = F1TvImageId(image.id),
            type = when (image.type) {
                THUMBNAIL -> Thumbnail
                CAR -> Car
                HEADSHOT -> Headshot
                else -> Unknown(image.type)
            },
            url = Uri.parse(image.url)
        )

    override suspend fun save(images: List<F1TvImage>) {
        val entities = images.map { toEntity(it) }
        dao.upsert(entities)
    }

    private fun toEntity(image: F1TvImage): ImageEntity =
        ImageEntity(
            id = image.id.value,
            type = when (image.type) {
                Thumbnail -> THUMBNAIL
                Car -> CAR
                Headshot -> HEADSHOT
                is Unknown -> image.type.value
            },
            url = image.url.toString()
        )

}