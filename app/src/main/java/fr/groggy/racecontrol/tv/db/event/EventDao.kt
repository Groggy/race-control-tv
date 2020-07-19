package fr.groggy.racecontrol.tv.db.event

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface EventDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsertAll(events: List<EventEntity>)

    @Query("SELECT * FROM events")
    suspend fun findAll(): List<EventEntity>

}