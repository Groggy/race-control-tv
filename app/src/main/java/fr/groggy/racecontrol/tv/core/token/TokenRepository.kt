package fr.groggy.racecontrol.tv.core.token

import fr.groggy.racecontrol.tv.f1.F1Token
import fr.groggy.racecontrol.tv.f1tv.F1TvToken

interface TokenRepository<T> {

    fun find(): T?

    fun save(token: T)

}

interface F1TokenRepository : TokenRepository<F1Token>

interface F1TvTokenRepository : TokenRepository<F1TvToken>