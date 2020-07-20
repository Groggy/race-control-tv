package fr.groggy.racecontrol.tv

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RaceControlTvApplication: Application() {

    companion object {
        private val TAG = RaceControlTvApplication::class.simpleName
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
    }

}