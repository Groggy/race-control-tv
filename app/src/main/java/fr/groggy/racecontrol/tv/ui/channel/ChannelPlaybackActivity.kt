package fr.groggy.racecontrol.tv.ui.channel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.f1tv.F1TvChannelId

@AndroidEntryPoint
class ChannelPlaybackActivity : FragmentActivity() {

    companion object {
        private val TAG = ChannelPlaybackActivity::class.simpleName

        private const val CHANNEL_ID = "channelId"

        fun intent(activity: Activity, channelId: F1TvChannelId): Intent {
            val intent = Intent(activity.baseContext, ChannelPlaybackActivity::class.java)
            intent.putExtra(CHANNEL_ID, channelId.value)
            return intent
        }

        fun getChannelId(activity: Activity): F1TvChannelId =
            F1TvChannelId(activity.intent.getStringExtra(CHANNEL_ID)!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_playback)
    }

}