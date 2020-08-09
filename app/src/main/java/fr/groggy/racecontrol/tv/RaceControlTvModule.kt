package fr.groggy.racecontrol.tv

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.noties.markwon.Markwon
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import java.net.CookiePolicy
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RaceControlTvModule {

    @Provides
    @Singleton
    fun clock(): Clock =
        Clock.systemUTC()

    @Provides
    @Singleton
    fun cookieManager(): CookieManager {
        val manager = CookieManager()
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        return manager
    }

    @Provides
    @Singleton
    fun okHttpClient(cookieManager: CookieManager): OkHttpClient =
        OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(cookieManager))
            .build()

    @Provides
    @Singleton
    fun moshi(): Moshi =
        Moshi.Builder().build()

    @Provides
    @Singleton
    fun httpDataSourceFactory(okHttpClient: OkHttpClient, @ApplicationContext context: Context): HttpDataSource.Factory =
        OkHttpDataSourceFactory(okHttpClient, context.resources.getString(R.string.app_name))

    @Provides
    fun downloadManager(@ApplicationContext context: Context): DownloadManager =
        context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

    @Provides
    fun localBroadcastManager(@ApplicationContext context: Context): LocalBroadcastManager =
        LocalBroadcastManager.getInstance(context)

    @Provides
    @Singleton
    fun markwon(@ApplicationContext context: Context): Markwon =
        Markwon.create(context)

}