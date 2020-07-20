package fr.groggy.racecontrol.tv.core

import android.util.Log
import fr.groggy.racecontrol.tv.core.token.TokenService
import fr.groggy.racecontrol.tv.f1tv.F1TvChannelId
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvViewing
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewingService @Inject constructor(
    private val f1Tv: F1TvClient,
    private val tokenService: TokenService
) {

    companion object {
        private val TAG = ViewingService::class.simpleName
    }

    suspend fun getViewing(channelId: F1TvChannelId): F1TvViewing {
        Log.d(TAG, "getViewing")
        val token = tokenService.loadAndGetF1TvToken()
        return f1Tv.getViewing(channelId, token)
    }

}