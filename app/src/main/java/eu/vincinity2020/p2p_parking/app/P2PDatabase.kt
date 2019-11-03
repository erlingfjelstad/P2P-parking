package eu.vincinity2020.p2p_parking.app

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.DB_NAME
import eu.vincinity2020.p2p_parking.data.dao.UserStopDao
import eu.vincinity2020.p2p_parking.data.entities.db.LatLngTypeConverters
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop

@Database(entities = [UserStop::class], version = App.DB_VERSION)
@TypeConverters(LatLngTypeConverters::class)
abstract class P2PDatabase: RoomDatabase() {

    abstract fun userStopDao(): UserStopDao

    companion object {
        val db = synchronized(P2PDatabase::class) {
            Room.databaseBuilder(
                    App.applicationContext(),
                    P2PDatabase::class.java, DB_NAME
            )
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}