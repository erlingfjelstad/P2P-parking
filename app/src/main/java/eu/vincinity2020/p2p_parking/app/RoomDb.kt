package eu.vincinity2020.p2p_parking.app

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.vincinity2020.p2p_parking.app.common.DateTypeConverter
import eu.vincinity2020.p2p_parking.data.dao.CarDao
import eu.vincinity2020.p2p_parking.data.dao.TripDao
import eu.vincinity2020.p2p_parking.data.entities.Car
import eu.vincinity2020.p2p_parking.data.entities.Trip


@TypeConverters(DateTypeConverter::class)
abstract class RoomDb : RoomDatabase() {
    abstract fun carDao(): CarDao

    abstract fun tripDao(): TripDao
}

