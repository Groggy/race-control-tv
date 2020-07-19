package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvDriverId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverService @Inject constructor(
    store: UpdatableStore<State>,
    private val f1TvClient: F1TvClient,
    private val imageService: ImageService
) {

    private val driversStore = store.lens(State.drivers)

    suspend fun loadDriverWithImages(id: F1TvDriverId) {
        val driver = f1TvClient.getDriver(id)
        driversStore.modify { it + (driver.id to driver) }
        imageService.loadImages(driver.images)
    }

}