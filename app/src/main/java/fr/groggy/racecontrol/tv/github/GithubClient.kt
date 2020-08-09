package fr.groggy.racecontrol.tv.github

import android.net.Uri
import android.util.Log
import com.squareup.moshi.Moshi
import fr.groggy.racecontrol.tv.core.release.ReleaseVersion
import fr.groggy.racecontrol.tv.utils.http.execute
import fr.groggy.racecontrol.tv.utils.http.parseJsonBody
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubClient @Inject constructor(
    private val httpClient: OkHttpClient,
    moshi: Moshi
) {

    companion object {
        private val TAG = GithubClient::class.simpleName
        private const val ROOT_URL = "https://api.github.com"
        private const val RACE_CONTROL_TV_REPO = "Groggy/race-control-tv"
        private const val APK_CONTENT_TYPE = "application/vnd.android.package-archive"
    }

    private val githubReleaseResponseJsonAdapter = moshi.adapter(GithubReleaseResponse::class.java)

    suspend fun getLatestRelease(): GithubRelease {
        val request = Request.Builder()
            .url("$ROOT_URL/repos/$RACE_CONTROL_TV_REPO/releases/latest")
            .get()
            .build()
        val response = request.execute(httpClient)
            .parseJsonBody(githubReleaseResponseJsonAdapter)
        Log.d(TAG, "Fetched release ${response.name}")
        return GithubRelease(
            id = GithubReleaseId(response.id),
            version = ReleaseVersion.of(response.name),
            description = response.body,
            apk = response.assets
                .find { it.contentType == APK_CONTENT_TYPE }
                ?.let { GithubAsset(
                    id = GithubAssetId(it.id),
                    name = it.name,
                    url = Uri.parse(it.browserDownloadUrl)
                ) }
        )
    }

}