package fr.groggy.racecontrol.tv.db.season

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface SeasonDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsertAll(seasons: List<SeasonEntity>)

    @Query("SELECT * FROM seasons")
    suspend fun findAll(): List<SeasonEntity>

}