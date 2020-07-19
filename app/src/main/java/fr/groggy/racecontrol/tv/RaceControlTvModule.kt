package fr.groggy.racecontrol.tv

import android.content.res.Resources
import arrow.optics.Optional
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import fr.groggy.racecontrol.tv.core.State
import fr.groggy.racecontrol.tv.core.Store
import fr.groggy.racecontrol.tv.core.UpdatableStore
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
    fun resources(): Resources =
        RaceControlTvApplication.resources

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
    fun httpDataSourceFactory(okHttpClient: OkHttpClient, resources: Resources): HttpDataSource.Factory =
        OkHttpDataSourceFactory(okHttpClient, resources.getString(R.string.app_name))

    @Provides
    @Singleton
    fun updatableStore(store: Store): UpdatableStore<State> =
        UpdatableStore(store, Optional.id())

}