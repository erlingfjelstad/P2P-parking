package eu.vincinity2020.p2p_parking.app

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.common.ApplicationScope
import eu.vincinity2020.p2p_parking.dao.CarDao
import eu.vincinity2020.p2p_parking.dao.TripDao
import eu.vincinity2020.p2p_parking.repositories.CarRepository
import eu.vincinity2020.p2p_parking.repositories.CarRepositoryImpl
import eu.vincinity2020.p2p_parking.repositories.TripRepository
import eu.vincinity2020.p2p_parking.repositories.TripRepositoryImpl

@Module
@ApplicationScope
class DbModule(private val context: Context, private val dbName: String) {

    @Provides
    @ApplicationScope
    fun providesRoomDb(): RoomDb {
        return Room.databaseBuilder(context, RoomDb::class.java, dbName).build()
    }

    @Provides
    @ApplicationScope
    fun providesCarDao(roomDb: RoomDb): CarDao {
        return roomDb.carDao()
    }

    @Provides
    @ApplicationScope
    fun providesCarRepository(carDao: CarDao): CarRepository {
        return CarRepositoryImpl(carDao)
    }

    @Provides
    @ApplicationScope
    fun providesTripDao(roomDb: RoomDb): TripDao {
        return roomDb.tripDao()
    }

    @Provides
    @ApplicationScope
    fun providesTripRepository(tripDao: TripDao): TripRepository {
        return TripRepositoryImpl(tripDao)
    }
}
