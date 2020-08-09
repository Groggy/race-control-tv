package fr.groggy.racecontrol.tv.db.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "releases")
data class ReleaseEntity(
    @PrimaryKey val version: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "apk_name") val apkName: String,
    @ColumnInfo(name = "apk_url") val apkUrl: String,
    @ColumnInfo(name = "dismissed") val dismissed: Boolean
)