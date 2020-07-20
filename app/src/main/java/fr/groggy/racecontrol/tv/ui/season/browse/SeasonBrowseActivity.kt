package fr.groggy.racecontrol.tv.ui.season.browse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId


@AndroidEntryPoint
class SeasonBrowseActivity : FragmentActivity() {

    companion object {
        private val TAG = SeasonBrowseActivity::class.simpleName

        fun intent(activity: Activity, seasonId: F1TvSeasonId): Intent {
            val intent = Intent(activity.baseContext, SeasonBrowseActivity::class.java)
            SeasonBrowseFragment.putSeasonId(
                intent,
                seasonId
            )
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_season_browse)
    }

}
