package fr.groggy.racecontrol.tv.core.driver

import fr.groggy.racecontrol.tv.core.FromState
import fr.groggy.racecontrol.tv.core.image.Image
import fr.groggy.racecontrol.tv.core.State
import fr.groggy.racecontrol.tv.f1tv.F1TvDriverId
import fr.groggy.racecontrol.tv.f1tv.F1TvImageType.Companion.Headshot

data class Driver(
    val id: F1TvDriverId,
    val name: String,
    val shortName: String,
    val racingNumber: Int,
    val images: List<Image>
) {

    companion object :
        FromState<F1TvDriverId, Driver> {
        override fun from(id: F1TvDriverId, state: State): Driver? =
            state.drivers[id]?.let {
                Driver(
                    id = it.id,
                    name = it.name,
                    shortName = it.shortName,
                    racingNumber = it.racingNumber,
                    images = Image.from(
                        it.images,
                        state
                    )
                )
            }
    }

    val headshot: Image?
        get() = images.find { it.type == Headshot }

}