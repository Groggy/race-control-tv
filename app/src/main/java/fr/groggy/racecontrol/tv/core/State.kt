package fr.groggy.racecontrol.tv.core

import arrow.optics.optics
import fr.groggy.racecontrol.tv.core.season.Season
import fr.groggy.racecontrol.tv.core.session.Session
import fr.groggy.racecontrol.tv.f1.F1Token
import fr.groggy.racecontrol.tv.f1tv.*

@optics
data class State(
    val hydrated: Boolean = false,
    val seasons: Map<F1TvSeasonId, F1TvSeason> = emptyMap(),
    val events: Map<F1TvEventId, F1TvEvent> = emptyMap(),
    val sessions: Map<F1TvSessionId, F1TvSession> = emptyMap(),
    val images: Map<F1TvImageId, F1TvImage> = emptyMap(),
    val channels: Map<F1TvChannelId, F1TvChannel> = emptyMap(),
    val drivers: Map<F1TvDriverId, F1TvDriver> = emptyMap(),
    val currentSeasonId: F1TvSeasonId? = null,
    val f1Token: F1Token? = null,
    val f1TvToken: F1TvToken? = null
) {
    companion object

    val currentSeason: Season?
        get() = currentSeasonId?.let { Season.from(it, this) }

    fun session(id: F1TvSessionId): Session? =
        Session.from(id, this)

}