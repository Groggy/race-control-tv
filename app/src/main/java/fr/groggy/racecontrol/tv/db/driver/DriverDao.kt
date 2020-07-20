package fr.groggy.racecontrol.tv.db.driver

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsert(driver: DriverEntity)

    @Query("SELECT * FROM drivers WHERE id = :id")
    fun observeById(id: String): Flow<DriverEntity?>

}