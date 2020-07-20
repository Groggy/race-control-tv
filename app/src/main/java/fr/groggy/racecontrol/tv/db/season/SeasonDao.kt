package fr.groggy.racecontrol.tv.db.season

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsert(season: SeasonEntity)

    @Query("SELECT * FROM seasons WHERE id = :id")
    fun observeById(id: String): Flow<SeasonEntity?>

}