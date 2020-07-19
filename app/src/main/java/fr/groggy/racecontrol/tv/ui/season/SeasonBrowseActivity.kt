package fr.groggy.racecontrol.tv.ui.season

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.R


@AndroidEntryPoint
class SeasonBrowseActivity : FragmentActivity() {

    companion object {
        private val TAG = SeasonBrowseActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_season_browse)
    }

}
