package fr.groggy.racecontrol.tv.core.image

import fr.groggy.racecontrol.tv.core.*
import fr.groggy.racecontrol.tv.utils.coroutines.concurrentMap
import fr.groggy.racecontrol.tv.f1tv.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageService @Inject constructor(
    store: UpdatableStore<State>,
    private val repository: ImageRepository,
    private val f1TvClient: F1TvClient
) : Hydratable {

    companion object {
        private val TAG = ImageService::class.simpleName
    }

    private val imagesStore = store.lens(State.images)

    override suspend fun hydrate() {
        imagesStore.hydrate(repository, TAG) { it.id }
        imagesStore.persistChanges(repository, TAG)
    }

    suspend fun loadImages(ids: List<F1TvImageId>) {
        val images = ids.concurrentMap { f1TvClient.getImage(it) }.associateBy { it.id }
        imagesStore.modify { it + images }
    }

}