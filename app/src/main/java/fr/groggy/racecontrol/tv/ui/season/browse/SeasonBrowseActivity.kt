package fr.groggy.racecontrol.tv.ui.season.browse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.core.season.SeasonService
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import fr.groggy.racecontrol.tv.utils.coroutines.schedule
import javax.inject.Inject
import kotlin.time.minutes


@AndroidEntryPoint
class SeasonBrowseActivity : FragmentActivity() {

    companion object {
        private val TAG = SeasonBrowseActivity::class.simpleName

        fun intent(activity: Activity): Intent =
            Intent(activity.baseContext, SeasonBrowseActivity::class.java)

        fun intent(activity: Activity, seasonId: F1TvSeasonId): Intent {
            val intent = intent(activity)
            SeasonBrowseFragment.putSeasonId(
                intent,
                seasonId
            )
            return intent
        }
    }

    @Inject lateinit var seasonService: SeasonService

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_season_browse)
        val viewModel: SeasonBrowseViewModel by viewModels()
        lifecycleScope.launchWhenCreated {
            SeasonBrowseFragment.findSeasonId(this@SeasonBrowseActivity)
                ?.let { viewModel.seasonLoaded(it) }
                ?: viewModel.currentSeasonLoaded()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SeasonBrowseFragment::class.java, null)
                .commit()
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        lifecycleScope.launchWhenStarted {
            schedule(1.minutes) {
                SeasonBrowseFragment.findSeasonId(this@SeasonBrowseActivity)
                    ?.let { seasonService.loadSeason(it) }
                    ?: seasonService.loadCurrentSeason()
            }
        }
    }

}
