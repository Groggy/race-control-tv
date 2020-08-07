package fr.groggy.racecontrol.tv.ui.player

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.analytics.AnalyticsListener.EventTime
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerControlView
import fr.groggy.racecontrol.tv.R
import kotlin.math.roundToInt

class CustomPlayerControlView(context: Context, attrs: AttributeSet) :
    PlayerControlView(context, attrs), AnalyticsListener {

    companion object {
        private val TAG = CustomPlayerControlView::class.simpleName
    }

    private val videoQualityView: TextView
    private val selectedAudioView: Button

    private var player: SimpleExoPlayer? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var onShowAudioSelectionDialogListener: (() -> Unit)? = null

    init {
        Log.d(TAG, "init")
        videoQualityView = findViewById(R.id.video_quality)
        selectedAudioView = findViewById(R.id.selected_audio)
        selectedAudioView?.setOnClickListener { _ ->
            onShowAudioSelectionDialogListener?.let { it() }
        }
    }

    override fun setPlayer(player: Player?) {
        Log.d(TAG, "setPlayer")
        super.setPlayer(player)
        this.player?.removeAnalyticsListener(this)
        this.player = player as SimpleExoPlayer?
        this.player?.addAnalyticsListener(this)
    }

    fun setTrackSelector(trackSelector: DefaultTrackSelector) {
        this.trackSelector = trackSelector
    }

    override fun onTracksChanged(eventTime: EventTime, trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
        Log.d(TAG, "onTracksChanged")
        val selectedAudio = trackSelections[1]
        if (selectedAudio == null) {
            selectedAudioView.visibility = View.INVISIBLE
        } else {
            selectedAudioView.text = selectedAudio.selectedFormat.label
            selectedAudioView.visibility = View.VISIBLE
            trackSelector?.currentMappedTrackInfo?.let {
                val audio = it.getTrackGroups(1)
                selectedAudioView.isEnabled = audio.length > 1
            }
        }
    }

    override fun onDownstreamFormatChanged(eventTime: EventTime, mediaLoadData: MediaLoadData) {
        Log.d(TAG, "onDownstreamFormatChanged")
        val trackFormat = mediaLoadData.trackFormat
        if (mediaLoadData.dataType != C.DATA_TYPE_MEDIA || trackFormat == null) {
            return
        }
        if (mediaLoadData.trackType == C.TRACK_TYPE_DEFAULT || mediaLoadData.trackType == C.TRACK_TYPE_VIDEO) {
            videoQualityView.text = context.getString(R.string.video_quality, trackFormat.height, trackFormat.frameRate.roundToInt())
            videoQualityView.visibility = View.VISIBLE
        }
    }

    fun onShowAudioSelectionDialog(listener: () -> Unit) {
        onShowAudioSelectionDialogListener = listener
    }

}