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

    override suspend fun findAll(): Set<F1TvImage> =
        dao.findAll()
            .map { F1TvImage(
                id = F1TvImageId(it.id),
                type = when(it.type) {
                    THUMBNAIL -> Thumbnail
                    CAR -> Car
                    HEADSHOT -> Headshot
                    else -> Unknown(it.type)
                },
                url = Uri.parse(it.url)
            ) }
            .toSet()

    override suspend fun save(set: Set<F1TvImage>) {
        val entities = set.map { ImageEntity(
            id = it.id.value,
            type = when(it.type) {
                Thumbnail -> THUMBNAIL
                Car -> CAR
                Headshot -> HEADSHOT
                is Unknown -> it.type.value
            },
            url = it.url.toString()
        ) }
        dao.upsertAll(entities)
    }

}