package eu.vincinity2020.p2p_parking.app

import androidx.room.Room
import androidx.room.RoomDatabase
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.DB_NAME

//@Database(entities = [], version = App.DB_VERSION)
abstract class P2PDatabase: RoomDatabase() {
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