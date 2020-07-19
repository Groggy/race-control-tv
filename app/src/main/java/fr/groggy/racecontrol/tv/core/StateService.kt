package fr.groggy.racecontrol.tv.core

import android.util.Log
import fr.groggy.racecontrol.tv.core.channel.ChannelService
import fr.groggy.racecontrol.tv.core.driver.DriverService
import fr.groggy.racecontrol.tv.core.event.EventService
import fr.groggy.racecontrol.tv.core.image.ImageService
import fr.groggy.racecontrol.tv.core.season.SeasonService
import fr.groggy.racecontrol.tv.core.session.SessionService
import fr.groggy.racecontrol.tv.core.token.TokenService
import fr.groggy.racecontrol.tv.utils.coroutines.concurrentForEach
import javax.inject.Inject
import javax.inject.Singleton

interface Hydratable {
    suspend fun hydrate()
}

@Singleton
class StateService @Inject constructor(
    store: UpdatableStore<State>,
    channelService: ChannelService,
    driverService: DriverService,
    eventService: EventService,
    imageService: ImageService,
    tokenService: TokenService,
    seasonService: SeasonService,
    sessionService: SessionService
) {

    companion object {
        private val TAG = StateService::class.simpleName
    }

    private val hydrated = store.lens(State.hydrated)

    private val hydratables = listOf(
        channelService,
        driverService,
        eventService,
        imageService,
        tokenService,
        seasonService,
        sessionService
    )

    suspend fun hydrate() {
        Log.i(TAG, "Hydrating state")
        hydratables.concurrentForEach { it.hydrate() }
        hydrated.set(true)
        Log.i(TAG, "State hydrated")
    }

}