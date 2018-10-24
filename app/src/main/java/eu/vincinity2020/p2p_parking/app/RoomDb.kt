package eu.vincinity2020.p2p_parking.app

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import eu.vincinity2020.p2p_parking.common.DateTypeConverter
import eu.vincinity2020.p2p_parking.dao.CarDao
import eu.vincinity2020.p2p_parking.dao.TripDao
import eu.vincinity2020.p2p_parking.entities.Car
import eu.vincinity2020.p2p_parking.entities.Trip

@Database(entities = [Car::class, Trip::class], version = App.DB_VERSION)
@TypeConverters(DateTypeConverter::class)
abstract class RoomDb : RoomDatabase() {
    abstract fun carDao(): CarDao

    abstract fun tripDao(): TripDao
}

