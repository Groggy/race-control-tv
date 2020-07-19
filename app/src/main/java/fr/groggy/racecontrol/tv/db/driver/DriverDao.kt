package fr.groggy.racecontrol.tv.db.driver

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface DriverDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsertAll(drivers: List<DriverEntity>)

    @Query("SELECT * FROM drivers")
    suspend fun findAll(): List<DriverEntity>

}