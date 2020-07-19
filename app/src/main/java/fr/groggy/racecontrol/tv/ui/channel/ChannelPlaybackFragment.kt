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
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.HttpDataSource
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.core.ViewingService
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChannelPlaybackFragment : Fragment() {

    companion object {
        private val TAG = ChannelPlaybackFragment::class.simpleName
    }

    @Inject lateinit var viewingService: ViewingService
    @Inject lateinit var httpDataSourceFactory: HttpDataSource.Factory

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        createPlayer()
    }

    private fun createPlayer() {
        val channelId = ChannelPlaybackActivity.getChannelId(requireActivity())
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.playWhenReady = true
        val mediaSourceFactory = HlsMediaSource.Factory(httpDataSourceFactory)
            .setAllowChunklessPreparation(true)
        lifecycleScope.launch {
            val viewing = viewingService.getViewing(channelId)
            val mediaSource = mediaSourceFactory.createMediaSource(viewing.url)
            player.prepare(mediaSource)
        }
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