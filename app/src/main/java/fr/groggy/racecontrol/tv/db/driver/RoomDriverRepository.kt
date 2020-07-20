package fr.groggy.racecontrol.tv.db.driver

import fr.groggy.racecontrol.tv.core.driver.DriverRepository
import fr.groggy.racecontrol.tv.db.IdListMapper
import fr.groggy.racecontrol.tv.db.RaceControlTvDatabase
import fr.groggy.racecontrol.tv.f1tv.F1TvDriver
import fr.groggy.racecontrol.tv.f1tv.F1TvDriverId
import fr.groggy.racecontrol.tv.f1tv.F1TvImageId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDriverRepository @Inject constructor(
    database: RaceControlTvDatabase,
    private val imageIdListMapper: IdListMapper<F1TvImageId>
) : DriverRepository {

    private val dao = database.driverDao()

    override fun observe(id: F1TvDriverId): Flow<F1TvDriver?> =
        dao.observeById(id.value)
            .map { driver -> driver?.let { toDriver(it) } }
            .distinctUntilChanged()

    private fun toDriver(driver: DriverEntity): F1TvDriver =
        F1TvDriver(
            id = F1TvDriverId(driver.id),
            name = driver.name,
            shortName = driver.shortName,
            racingNumber = driver.racingNumber,
            images = imageIdListMapper.fromDto(driver.images)
        )

    override suspend fun save(driver: F1TvDriver) {
        val entitiy = toEntity(driver)
        dao.upsert(entitiy)
    }

    private fun toEntity(driver: F1TvDriver): DriverEntity =
        DriverEntity(
            id = driver.id.value,
            name = driver.name,
            shortName = driver.shortName,
            racingNumber = driver.racingNumber,
            images = imageIdListMapper.toDto(driver.images)
        )

}