package fr.groggy.racecontrol.tv.core.driver

import fr.groggy.racecontrol.tv.f1tv.F1TvDriver
import fr.groggy.racecontrol.tv.f1tv.F1TvDriverId
import kotlinx.coroutines.flow.Flow

interface DriverRepository {

    fun observe(id: F1TvDriverId): Flow<F1TvDriver?>

    suspend fun save(driver: F1TvDriver)

}