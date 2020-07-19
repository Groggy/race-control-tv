package fr.groggy.racecontrol.tv.db.driver

import fr.groggy.racecontrol.tv.core.driver.DriverRepository
import fr.groggy.racecontrol.tv.db.IdListMapper
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import fr.groggy.racecontrol.tv.f1tv.F1TvDriver
import fr.groggy.racecontrol.tv.f1tv.F1TvDriverId
import fr.groggy.racecontrol.tv.f1tv.F1TvImageId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDriverRepository @Inject constructor(
    database: RaceControlTvDatabase,
    private val imageIdListMapper: IdListMapper<F1TvImageId>
) : DriverRepository {

    private val dao = database.driverDao()

    override suspend fun findAll(): Set<F1TvDriver> =
        dao.findAll()
            .map { F1TvDriver(
                id = F1TvDriverId(it.id),
                name = it.name,
                shortName = it.shortName,
                racingNumber = it.racingNumber,
                images = imageIdListMapper.fromDto(it.images)
            ) }
            .toSet()

    override suspend fun save(set: Set<F1TvDriver>) {
        val entities = set.map { DriverEntity(
            id = it.id.value,
            name = it.name,
            shortName = it.shortName,
            racingNumber = it.racingNumber,
            images = imageIdListMapper.toDto(it.images)
        ) }
        dao.upsertAll(entities)
    }

}