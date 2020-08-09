package fr.groggy.racecontrol.tv.core.release

interface ReleaseRepository {

    suspend fun find(version: ReleaseVersion): Release?

    suspend fun save(release: Release)

}