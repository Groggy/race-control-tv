package fr.groggy.racecontrol.tv.core.credentials

import fr.groggy.racecontrol.tv.f1.F1Credentials

interface F1CredentialsRepository {

    fun find(): F1Credentials?

    fun save(credentials: F1Credentials)

    fun delete()

}