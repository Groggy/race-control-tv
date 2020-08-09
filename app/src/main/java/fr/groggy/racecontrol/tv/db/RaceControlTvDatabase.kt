package fr.groggy.racecontrol.tv.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.groggy.racecontrol.tv.db.channel.ChannelDao
import fr.groggy.racecontrol.tv.db.channel.ChannelEntity
import fr.groggy.racecontrol.tv.db.driver.DriverDao
import fr.groggy.racecontrol.tv.db.driver.DriverEntity
import fr.groggy.racecontrol.tv.db.event.EventDao
import fr.groggy.racecontrol.tv.db.event.EventEntity
import fr.groggy.racecontrol.tv.db.image.ImageDao
import fr.groggy.racecontrol.tv.db.image.ImageEntity
import fr.groggy.racecontrol.tv.db.release.ReleaseDao
import fr.groggy.racecontrol.tv.db.release.ReleaseEntity
import fr.groggy.racecontrol.tv.db.season.SeasonDao
import fr.groggy.racecontrol.tv.db.season.SeasonEntity
import fr.groggy.racecontrol.tv.db.session.SessionDao
import fr.groggy.racecontrol.tv.db.session.SessionEntity

@Database(
    entities = [
        ChannelEntity::class,
        DriverEntity::class,
        EventEntity::class,
        ImageEntity::class,
        ReleaseEntity::class,
        SeasonEntity::class,
        SessionEntity::class
    ],
    version = 2
)
abstract class RaceControlTvDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE releases (
                     version TEXT PRIMARY KEY NOT NULL,
                     description TEXT NOT NULL,
                     apk_name TEXT NOT NULL,
                     apk_url TEXT NOT NULL,
                     dismissed INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }
    }

    abstract fun channelDao(): ChannelDao

    abstract fun driverDao(): DriverDao

    abstract fun eventDao(): EventDao

    abstract fun imageDao(): ImageDao

    abstract fun releaseDao(): ReleaseDao

    abstract fun seasonDao(): SeasonDao

    abstract fun sessionDao(): SessionDao

}