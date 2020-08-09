package fr.groggy.racecontrol.tv.db.release

import android.net.Uri
import fr.groggy.racecontrol.tv.core.release.Apk
import fr.groggy.racecontrol.tv.core.release.Release
import fr.groggy.racecontrol.tv.core.release.ReleaseRepository
import fr.groggy.racecontrol.tv.core.release.ReleaseVersion
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomReleaseRepository @Inject constructor(
    database: RaceControlTvDatabase
) : ReleaseRepository {

    private val dao = database.releaseDao()

    override suspend fun find(version: ReleaseVersion): Release? =
        dao.findByVersion(version.stringValue)
            ?.let { toRelease(it) }

    private fun toRelease(release: ReleaseEntity): Release =
        Release(
            version = ReleaseVersion.of(release.version),
            description = release.description,
            apk = Apk(
                name = release.apkName,
                url = Uri.parse(release.apkUrl)
            ),
            dismissed = release.dismissed
        )

    override suspend fun save(release: Release) {
        val entity = toEntity(release)
        dao.upsert(entity)
    }

    private fun toEntity(release: Release): ReleaseEntity =
        ReleaseEntity(
            version = release.version.stringValue,
            description = release.description,
            apkName = release.apk.name,
            apkUrl = release.apk.url.toString(),
            dismissed = release.dismissed
        )

}