package fr.groggy.racecontrol.tv.db.channel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ChannelDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsertAll(channels: List<ChannelEntity>)

    @Query("SELECT * FROM channels")
    suspend fun findAll(): List<ChannelEntity>

}