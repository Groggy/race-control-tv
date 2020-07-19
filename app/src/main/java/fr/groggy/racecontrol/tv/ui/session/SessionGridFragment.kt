package fr.groggy.racecontrol.tv.ui.session

import android.os.Bundle
import android.util.Log
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.core.Channel
import fr.groggy.racecontrol.tv.core.Session
import fr.groggy.racecontrol.tv.core.SessionService
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId
import fr.groggy.racecontrol.tv.ui.UiObservableStore
import fr.groggy.racecontrol.tv.ui.channel.ChannelCardPresenter
import fr.groggy.racecontrol.tv.ui.channel.ChannelDiffCallback
import fr.groggy.racecontrol.tv.ui.channel.ChannelPlaybackActivity
import javax.inject.Inject


@AndroidEntryPoint
class SessionGridFragment : VerticalGridSupportFragment(), OnItemViewClickedListener {

    companion object {
        private val TAG = SessionGridFragment::class.simpleName

        private const val COLUMNS = 5
    }

    @Inject lateinit var store: UiObservableStore
    @Inject lateinit var sessionService: SessionService
    @Inject lateinit var channelsCardPresenter: ChannelCardPresenter
    @Inject lateinit var channelDiffCallback: ChannelDiffCallback

    private val sessionId: F1TvSessionId by lazy { SessionBrowseActivity.getSessionId(requireActivity()) }

    private lateinit var channelsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setupUIElements()
        setupEventListeners()
        store.observe { it.session(sessionId) }.subscribe { onUpdatedSession(it) }
    }

    private fun setupUIElements() {
        gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = COLUMNS
        channelsAdapter = ArrayObjectAdapter(channelsCardPresenter)
        adapter = channelsAdapter
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = this
    }

    private fun onUpdatedSession(session: Session) {
        title = session.name
        channelsAdapter.setItems(session.channels, channelDiffCallback)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        lifecycleScope.launchWhenStarted { sessionService.loadSessionWithImagesAndChannels(sessionId) }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        store.dispose()
    }

    override fun onItemClicked(itemViewHolder: Presenter.ViewHolder?, item: Any, rowViewHolder: RowPresenter.ViewHolder?, row: Row?) {
        val channel = item as Channel
        val intent = ChannelPlaybackActivity.intent(requireActivity(), channel.id)
        startActivity(intent)
    }

}