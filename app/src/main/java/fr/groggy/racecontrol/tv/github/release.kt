package fr.groggy.racecontrol.tv.github

import android.net.Uri
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import fr.groggy.racecontrol.tv.core.release.ReleaseVersion

@JsonClass(generateAdapter = true)
data class GithubReleaseResponse(
    val id: Long,
    val name: String,
    val body: String,
    val assets: List<GithubAssetResponse>
)

@JsonClass(generateAdapter = true)
data class GithubAssetResponse(
    val id: Long,
    val name: String,
    @Json(name = "content_type") val contentType: String,
    @Json(name = "browser_download_url") val browserDownloadUrl: String
)

inline class GithubReleaseId(val value: Long)

data class GithubRelease(
    val id: GithubReleaseId,
    val version: ReleaseVersion,
    val description: String,
    val apk: GithubAsset?
)

inline class GithubAssetId(val value: Long)

data class GithubAsset(
    val id: GithubAssetId,
    val name: String,
    val url: Uri
)