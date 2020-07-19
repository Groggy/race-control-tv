package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.core.token.TokenService
import fr.groggy.racecontrol.tv.f1tv.F1TvChannelId
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvViewing
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewingService @Inject constructor(
    private val f1TvClient: F1TvClient,
    private val tokenService: TokenService
) {

    suspend fun getViewing(channelId: F1TvChannelId): F1TvViewing {
        val token = tokenService.loadAndGetF1TvToken()
        return f1TvClient.getViewing(channelId, token)
    }

}