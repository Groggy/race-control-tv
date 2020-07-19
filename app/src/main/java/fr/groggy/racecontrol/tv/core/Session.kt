package fr.groggy.racecontrol.tv.core

import arrow.optics.optics
import fr.groggy.racecontrol.tv.f1tv.F1TvImageType.Companion.Thumbnail
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionStatus.Companion.Live

@optics
data class Session(
    val id: F1TvSessionId,
    val name: String,
    val live: Boolean,
    val period: InstantPeriod,
    val images: List<Image>,
    val channels: List<Channel>
) {

    companion object : FromState<F1TvSessionId, Session> {
        override fun from(id: F1TvSessionId, state: State): Session? =
            state.sessions[id]?.let { Session(
                id = it.id,
                name = it.name,
                live = it.status == Live,
                period = it.period,
                images = Image.from(it.images, state),
                channels = Channel.from(it.channels, state)
            ) }
    }

    val thumbnail: Image?
        get() = images.find { it.type == Thumbnail }

}