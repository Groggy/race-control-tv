package fr.groggy.racecontrol.tv.ui.season

import android.os.Bundle
import android.util.Log
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.core.Event
import fr.groggy.racecontrol.tv.core.Season
import fr.groggy.racecontrol.tv.core.SeasonService
import fr.groggy.racecontrol.tv.core.Session
import fr.groggy.racecontrol.tv.ui.*
import fr.groggy.racecontrol.tv.ui.event.EventListRowDiffCallback
import fr.groggy.racecontrol.tv.ui.session.SessionBrowseActivity
import fr.groggy.racecontrol.tv.ui.session.SessionCardPresenter
import fr.groggy.racecontrol.tv.ui.session.SessionDiffCallback
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeasonBrowseFragment : BrowseSupportFragment(), OnItemViewClickedListener {

    companion object {
        private val TAG = SeasonBrowseFragment::class.simpleName
    }

    @Inject lateinit var store: UiObservableStore
    @Inject lateinit var seasonService: SeasonService
    @Inject lateinit var eventListRowDiffCallback: EventListRowDiffCallback
    @Inject lateinit var sessionDiffCallback: SessionDiffCallback
    @Inject lateinit var sessionCardPresenter: SessionCardPresenter

    private lateinit var eventsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { seasonService.loadCurrentSeason() }
        setupUIElements()
        setupEventListeners()
        store.observe { it.currentSeason }.subscribe { updateSeason(it) }
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

    private fun updateSeason(season: Season) {
        title = season.name
        val existingListRows = eventsAdapter.unmodifiableList<ListRow>()
        val events = season.events
            .filter { it.sessions.isNotEmpty() }
            .map { toListRow(it, existingListRows) }
        eventsAdapter.setItems(events, eventListRowDiffCallback)
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
        sessionsAdapter.setItems(event.sessions, sessionDiffCallback)
        return listRow
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        store.dispose()
    }

    override fun onItemClicked(itemViewHolder: Presenter.ViewHolder, item: Any, rowViewHolder: RowPresenter.ViewHolder, row: Row) {
        val session = item as Session
        val intent =
            SessionBrowseActivity.intent(
                requireActivity(),
                session.id
            )
        startActivity(intent)
    }

}