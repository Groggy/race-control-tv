package fr.groggy.racecontrol.tv.ui.channel

import android.util.Log
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ImageCardView.CARD_TYPE_FLAG_CONTENT
import androidx.leanback.widget.ImageCardView.CARD_TYPE_FLAG_TITLE
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import fr.groggy.racecontrol.tv.core.channel.BasicChannel
import fr.groggy.racecontrol.tv.core.channel.Channel
import fr.groggy.racecontrol.tv.core.channel.OnboardChannel
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.Data
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.PitLane
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.Tracker
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.Unknown
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType.Companion.Wif
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelCardPresenter @Inject constructor() : Presenter() {

    companion object {
        private val TAG = ChannelCardPresenter::class.simpleName

        private const val WIDTH = 313
        private const val HEIGHT = 274
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val view = ImageCardView(parent.context)
        view.setMainImageDimensions(
            WIDTH,
            HEIGHT
        )
        view.cardType = CARD_TYPE_FLAG_TITLE or CARD_TYPE_FLAG_CONTENT
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        Log.d(TAG, "onBindViewHolder")
        val view = viewHolder.view as ImageCardView
        val channel = item as Channel
        when(channel) {
            is BasicChannel -> {
                view.titleText = when(channel.type) {
                    Wif -> "Main broadcast"
                    PitLane -> "Pit lane"
                    Tracker -> "Tracker"
                    Data -> "Data"
                    is Unknown -> channel.type.name
                }
            }
            is OnboardChannel -> {
                view.titleText = channel.name
                view.contentText = channel.driver.racingNumber.toString()
                channel.driver.headshot?.let {
                    Glide.with(viewHolder.view.context)
                        .load(it.url)
                        .centerCrop()
                        .into(view.mainImageView)
                }
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val view = viewHolder.view as ImageCardView
        view.titleText = null
        view.contentText = null
        view.badgeImage = null
        view.mainImage = null
    }

}