package fr.groggy.racecontrol.tv.db.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsert(images: List<ImageEntity>)

    @Query("SELECT * FROM images WHERE id IN (:ids)")
    fun observeById(ids: List<String>): Flow<List<ImageEntity>>

}