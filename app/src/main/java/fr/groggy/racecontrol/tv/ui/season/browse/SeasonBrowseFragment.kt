package fr.groggy.racecontrol.tv.ui.season.browse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.Keep
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.core.season.SeasonService
import fr.groggy.racecontrol.tv.f1tv.F1TvSeasonId
import fr.groggy.racecontrol.tv.ui.channel.playback.ChannelPlaybackActivity
import fr.groggy.racecontrol.tv.ui.event.EventListRowDiffCallback
import fr.groggy.racecontrol.tv.ui.session.browse.SessionBrowseActivity
import fr.groggy.racecontrol.tv.ui.session.SessionCardPresenter
import javax.inject.Inject

@Keep
@AndroidEntryPoint
class SeasonBrowseFragment : BrowseSupportFragment(), OnItemViewClickedListener {

    companion object {
        private val TAG = SeasonBrowseFragment::class.simpleName

        private val SEASON_ID = "${SeasonBrowseFragment::class}.SEASON_ID"

        fun putSeasonId(intent: Intent, seasonId: F1TvSeasonId) {
            intent.putExtra(SEASON_ID, seasonId.value)
        }

        fun findSeasonId(activity: Activity): F1TvSeasonId? =
            activity.intent.getStringExtra(SEASON_ID)?.let { F1TvSeasonId(it) }
    }

    @Inject lateinit var seasonService: SeasonService
    @Inject lateinit var eventListRowDiffCallback: EventListRowDiffCallback
    @Inject lateinit var sessionCardPresenter: SessionCardPresenter

    private lateinit var eventsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setupUIElements()
        setupEventListeners()

        val viewModel: SeasonBrowseViewModel by viewModels()
        val season = findSeasonId(requireActivity())
            ?.let { viewModel.season(it) }
            ?: viewModel.currentSeason
        season.asLiveData().observe(this, Observer { onUpdatedSeason(it) })
    }

    private fun setupUIElements() {
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = false
        brandColor = resources.getColor(R.color.fastlane_background, null)
        eventsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = eventsAdapter
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = this
    }

    private fun onUpdatedSeason(season: Season) {
        title = season.name
        val existingListRows = eventsAdapter.unmodifiableList<ListRow>()
        val events = season.events
            .filter { it.sessions.isNotEmpty() }
            .map { toListRow(it, existingListRows) }
        eventsAdapter.setItems(events, eventListRowDiffCallback)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        lifecycleScope.launchWhenStarted {
            findSeasonId(requireActivity())
                ?.let { seasonService.loadSeason(it) }
                ?: seasonService.loadCurrentSeason()
        }
    }

    private fun toListRow(event: Event, existingListRows: List<ListRow>): ListRow {
        val existingListRow = existingListRows.find { it.headerItem.name == event.name }
        val (listRow, sessionsAdapter) = if (existingListRow == null) {
            val sessionsAdapter = ArrayObjectAdapter(sessionCardPresenter)
            val listRow = ListRow(HeaderItem(event.name), sessionsAdapter)
            listRow to sessionsAdapter
        } else {
            val sessionsAdapter = existingListRow.adapter as ArrayObjectAdapter
            existingListRow to sessionsAdapter
        }
        sessionsAdapter.setItems(event.sessions, Session.diffCallback)
        return listRow
    }

    override fun onItemClicked(itemViewHolder: Presenter.ViewHolder, item: Any, rowViewHolder: RowPresenter.ViewHolder, row: Row) {
        val session = item as Session
        val intent = session.channels.singleOrNull()
            ?.let { ChannelPlaybackActivity.intent(requireActivity(), it) }
            ?: SessionBrowseActivity.intent(requireActivity(), session.id)
        startActivity(intent)
    }

}