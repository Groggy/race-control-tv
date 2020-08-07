package fr.groggy.racecontrol.tv.ui.player

import android.content.Context
import android.util.AttributeSet
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import fr.groggy.racecontrol.tv.R

class CustomPlayerView(context: Context, attrs: AttributeSet) :
    PlayerView(context, attrs) {

    private val controller: CustomPlayerControlView by lazy {
        findViewById<CustomPlayerControlView>(R.id.exo_controller)
    }

    fun setTrackSelector(trackSelector: DefaultTrackSelector) {
        controller.setTrackSelector(trackSelector)
    }

    fun onShowAudioSelectionDialog(listener: () -> Unit) {
        controller.onShowAudioSelectionDialog(listener)
    }

}