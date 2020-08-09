package fr.groggy.racecontrol.tv.ui.session.browse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.Keep
import androidx.fragment.app.viewModels
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.asLiveData
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId
import fr.groggy.racecontrol.tv.ui.channel.ChannelCardPresenter
import fr.groggy.racecontrol.tv.ui.channel.playback.ChannelPlaybackActivity
import javax.inject.Inject


@Keep
@AndroidEntryPoint
class SessionGridFragment : VerticalGridSupportFragment(), OnItemViewClickedListener {

    companion object {
        private val TAG = SessionGridFragment::class.simpleName

        private const val COLUMNS = 5

        private val SESSION_ID = "${SessionGridFragment::class}.SESSION_ID"

        fun putSessionId(intent: Intent, sessionId: F1TvSessionId) {
            intent.putExtra(SESSION_ID, sessionId.value)
        }

        fun findSessionId(activity: Activity): F1TvSessionId? =
            activity.intent.getStringExtra(SESSION_ID)?.let { F1TvSessionId(it) }
    }

    @Inject lateinit var channelsCardPresenter: ChannelCardPresenter

    private val sessionId: F1TvSessionId by lazy { findSessionId(requireActivity())!! }

    private lateinit var channelsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setupUIElements()
        setupEventListeners()
        val viewModel: SessionBrowseViewModel by viewModels({ requireActivity() })
        viewModel.session(sessionId).asLiveData().observe(this, this::onUpdatedSession)
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
        when(session) {
            is SingleChannelSession -> {
                val intent = ChannelPlaybackActivity.intent(requireActivity(), session.channel)
                startActivity(intent)
                requireActivity().finish()
            }
            is MultiChannelsSession -> {
                title = session.name
                channelsAdapter.setItems(session.channels, Channel.diffCallback)
            }
        }
    }

    override fun onItemClicked(itemViewHolder: Presenter.ViewHolder?, item: Any, rowViewHolder: RowPresenter.ViewHolder?, row: Row?) {
        val channel = item as Channel
        val intent = ChannelPlaybackActivity.intent(requireActivity(), channel.id)
        startActivity(intent)
    }

}