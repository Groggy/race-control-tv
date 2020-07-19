package fr.groggy.racecontrol.tv.db.driver

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "short_name") val shortName: String,
    @ColumnInfo(name = "racing_number") val racingNumber: Int,
    @ColumnInfo(name = "images") val images: String
)