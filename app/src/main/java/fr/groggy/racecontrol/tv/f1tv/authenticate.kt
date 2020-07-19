package fr.groggy.racecontrol.tv.f1tv

import com.auth0.android.jwt.JWT
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class F1TvAuthenticateRequest(
    @Json(name = "identity_provider_url") val identityProviderUrl: String,
    @Json(name = "access_token") val accessToken: String)

@JsonClass(generateAdapter = true)
data class F1TvAuthenticateResponse(val token: String)

inline class F1TvToken(val value: JWT)