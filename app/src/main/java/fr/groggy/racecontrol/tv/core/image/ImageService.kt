package fr.groggy.racecontrol.tv.core.image

import android.util.Log
import fr.groggy.racecontrol.tv.f1tv.F1TvClient
import fr.groggy.racecontrol.tv.f1tv.F1TvImageId
import fr.groggy.racecontrol.tv.utils.coroutines.concurrentMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageService @Inject constructor(
    private val repository: ImageRepository,
    private val f1Tv: F1TvClient
) {

    companion object {
        private val TAG = ImageService::class.simpleName
    }

    suspend fun loadImages(ids: List<F1TvImageId>) {
        Log.d(TAG, "loadImages")
        val images = ids.concurrentMap { f1Tv.getImage(it) }
        repository.save(images)
    }

}