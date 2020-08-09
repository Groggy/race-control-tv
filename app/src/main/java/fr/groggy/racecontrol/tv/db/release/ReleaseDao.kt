package fr.groggy.racecontrol.tv.db.release

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ReleaseDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsert(release: ReleaseEntity)

    @Query("SELECT * FROM releases WHERE version = :version")
    suspend fun findByVersion(version: String): ReleaseEntity?

}