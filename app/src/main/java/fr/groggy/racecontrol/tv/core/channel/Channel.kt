package fr.groggy.racecontrol.tv.core.channel

import arrow.optics.Getter
import fr.groggy.racecontrol.tv.core.driver.Driver
import fr.groggy.racecontrol.tv.core.FromState
import fr.groggy.racecontrol.tv.core.State
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannel
import fr.groggy.racecontrol.tv.f1tv.F1TvBasicChannelType
import fr.groggy.racecontrol.tv.f1tv.F1TvChannelId
import fr.groggy.racecontrol.tv.f1tv.F1TvOnboardChannel

sealed class Channel {

    companion object :
        FromState<F1TvChannelId, Channel> {
        val id = Getter(Channel::id)

        override fun from(id: F1TvChannelId, state: State): Channel? =
            state.channels[id]?.let { when(it) {
                is F1TvBasicChannel -> BasicChannel(
                    id = it.id,
                    type = it.type
                )
                is F1TvOnboardChannel -> Driver.from(
                    it.driver,
                    state
                )?.let { driver ->
                    OnboardChannel(
                        id = it.id,
                        name = it.name,
                        driver = driver
                    )
                }
            } }
    }

    abstract val id: F1TvChannelId

}

data class BasicChannel(
    override val id: F1TvChannelId,
    val type: F1TvBasicChannelType
) : Channel()

data class OnboardChannel(
    override val id: F1TvChannelId,
    val name: String,
    val driver: Driver
) : Channel()