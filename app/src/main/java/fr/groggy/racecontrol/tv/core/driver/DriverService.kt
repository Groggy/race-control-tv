package fr.groggy.racecontrol.tv.core.driver

import android.util.Log
import fr.groggy.racecontrol.tv.core.image.ImageService
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvDriverId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverService @Inject constructor(
    private val repository: DriverRepository,
    private val f1Tv: F1TvClient,
    private val imageService: ImageService
) {

    companion object {
        private val TAG = DriverService::class.simpleName
    }

    suspend fun loadDriverWithImages(id: F1TvDriverId) {
        Log.d(TAG, "loadDriverWithImages")
        val driver = f1Tv.getDriver(id)
        repository.save(driver)
        imageService.loadImages(driver.images)
    }

}