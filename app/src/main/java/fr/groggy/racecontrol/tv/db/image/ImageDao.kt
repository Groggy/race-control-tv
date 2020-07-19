package fr.groggy.racecontrol.tv.db.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsertAll(images: List<ImageEntity>)

    @Query("SELECT * FROM images")
    suspend fun findAll(): List<ImageEntity>

}