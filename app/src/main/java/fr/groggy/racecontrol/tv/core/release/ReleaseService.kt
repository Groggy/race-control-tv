package fr.groggy.racecontrol.tv.core.release

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import fr.groggy.racecontrol.tv.BuildConfig
import fr.groggy.racecontrol.tv.github.GithubClient
import net.swiftzer.semver.SemVer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleaseService @Inject constructor(
    private val releaseRepository: ReleaseRepository,
    private val client: GithubClient
) {

    companion object {
        private val TAG = ReleaseService::class.simpleName
    }

    private val currentVersion by lazy {
        ReleaseVersion.of(BuildConfig.VERSION_NAME)
    }

    suspend fun findNewRelease(): Release? {
        val latestRelease = client.getLatestRelease()
        Log.d(TAG, "Latest release is $latestRelease")
        if (latestRelease.apk == null ||
            currentVersion.debug ||
            latestRelease.version <= currentVersion ||
            isDismissed(latestRelease.version)) {
            return null
        }
        val release = Release(
            version = latestRelease.version,
            description = latestRelease.description,
            apk = Apk(
                name = latestRelease.apk.name,
                url = latestRelease.apk.url
            ),
            dismissed = false
        )
        releaseRepository.save(release)
        return release
    }

    private suspend fun isDismissed(version: ReleaseVersion) =
        releaseRepository.find(version)?.dismissed ?: false

    suspend fun dismiss(version: ReleaseVersion) {
        releaseRepository.find(version)?.let {
            val dismissed = it.copy(dismissed = true)
            releaseRepository.save(dismissed)
        }
    }

}

inline class ReleaseVersion(val value: SemVer) : Comparable<ReleaseVersion> {

    companion object {
        fun of(version: String): ReleaseVersion =
            ReleaseVersion(SemVer.parse(version))
    }

    val stringValue: String
        get() = value.toString()

    val debug: Boolean
        get() = value.preRelease == "DEBUG"

    override fun compareTo(other: ReleaseVersion): Int =
        value.compareTo(other.value)

}

data class Release(
    val version: ReleaseVersion,
    val description: String,
    val apk: Apk,
    val dismissed: Boolean
)

data class Apk(
    val name: String,
    val url: Uri
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Apk> {
        override fun createFromParcel(parcel: Parcel): Apk =
            Apk(
                name = parcel.readString()!!,
                url = Uri.parse(parcel.readString())
            )

        override fun newArray(size: Int): Array<Apk?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url.toString())
    }

    override fun describeContents(): Int {
        return hashCode()
    }

}