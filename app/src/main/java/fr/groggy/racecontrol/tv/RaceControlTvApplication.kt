package fr.groggy.racecontrol.tv

import android.app.Application
import android.content.res.Resources
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import fr.groggy.racecontrol.tv.f1.F1Client
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import java.time.Clock
import javax.inject.Inject

@HiltAndroidApp
class RaceControlTvApplication: Application() {

    companion object {
        private val TAG = RaceControlTvApplication::class.simpleName
        lateinit var resources: Resources
    }

    @Inject lateinit var clock: Clock
    @Inject lateinit var f1Client: F1Client
    @Inject lateinit var f1TvClient: F1TvClient

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
        RaceControlTvApplication.resources = resources
    }

}