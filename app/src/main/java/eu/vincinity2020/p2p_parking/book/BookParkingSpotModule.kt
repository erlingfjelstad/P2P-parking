package eu.vincinity2020.p2p_parking.book

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.common.ActivityScope
import eu.vincinity2020.p2p_parking.repositories.CarRepository
import eu.vincinity2020.p2p_parking.repositories.TripRepository

@Module
class BookParkingSpotModule {

    @Provides
    @ActivityScope
    fun providesBookParkingSpotPresenter(tripRepository: TripRepository,
                                         carRepository: CarRepository): BookParkingSpotPresenter {
        return BookParkingSpotPresenterImpl(tripRepository, carRepository)
    }
}