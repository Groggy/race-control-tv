package fr.groggy.racecontrol.tv.core.driver

import fr.groggy.racecontrol.tv.core.*
import fr.groggy.racecontrol.tv.core.image.ImageService
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvDriverId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverService @Inject constructor(
    store: UpdatableStore<State>,
    private val repository: DriverRepository,
    private val f1Tv: F1TvClient,
    private val imageService: ImageService
) : Hydratable {

    companion object {
        private val TAG = DriverService::class.simpleName
    }

    private val driversStore = store.lens(State.drivers)

    override suspend fun hydrate() {
        driversStore.hydrate(repository, TAG) { it.id }
        driversStore.persistChanges(repository, TAG)
    }

    suspend fun loadDriverWithImages(id: F1TvDriverId) {
        val driver = f1Tv.getDriver(id)
        driversStore.modify { it + (driver.id to driver) }
        imageService.loadImages(driver.images)
    }

}