package fr.groggy.racecontrol.tv.db.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface SessionDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsertAll(sessions: List<SessionEntity>)

    @Query("SELECT * FROM sessions")
    suspend fun findAll(): List<SessionEntity>

}