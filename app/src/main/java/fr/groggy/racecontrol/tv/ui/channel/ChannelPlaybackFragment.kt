package fr.groggy.racecontrol.tv.ui.channel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.HttpDataSource
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.core.ViewingService
import fr.groggy.racecontrol.tv.f1tv.F1TvViewing
import javax.inject.Inject

@AndroidEntryPoint
class ChannelPlaybackFragment : Fragment() {

    companion object {
        private val TAG = ChannelPlaybackFragment::class.simpleName
    }

    @Inject lateinit var viewingService: ViewingService
    @Inject lateinit var httpDataSourceFactory: HttpDataSource.Factory

    private lateinit var player: ExoPlayer
    private lateinit var mediaSourceFactory: MediaSourceFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        createPlayer()
    }

    private fun createPlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.playWhenReady = true
        mediaSourceFactory = HlsMediaSource.Factory(httpDataSourceFactory)
            .setAllowChunklessPreparation(true)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        val channelId = ChannelPlaybackActivity.getChannelId(requireActivity())
        lifecycleScope.launchWhenStarted {
            val viewing = viewingService.getViewing(channelId)
            onViewingCreated(viewing)
        }
    }

    private fun onViewingCreated(viewing: F1TvViewing) {
        val mediaSource = mediaSourceFactory.createMediaSource(viewing.url)
        player.prepare(mediaSource)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        val view = PlayerView(requireContext())
        view.player = player
        return view
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

}