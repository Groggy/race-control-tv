package fr.groggy.racecontrol.tv.core

import fr.groggy.racecontrol.tv.concurrentMap
import fr.groggy.racecontrol.tv.f1tv.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageService @Inject constructor(
    store: UpdatableStore<State>,
    private val f1TvClient: F1TvClient
) {

    private val imagesStore = store.lens(State.images)

    suspend fun loadImages(ids: List<F1TvImageId>) {
        val images = ids.concurrentMap { f1TvClient.getImage(it) }.associateBy { it.id }
        imagesStore.modify { it + images }
    }

}