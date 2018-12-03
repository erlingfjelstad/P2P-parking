package eu.vincinity2020.p2p_parking.ui.book

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.data.repositories.CarRepository
import eu.vincinity2020.p2p_parking.data.repositories.TripRepository

@Module
class BookParkingSpotModule {

    @Provides
    @ActivityScope
    fun providesBookParkingSpotPresenter(tripRepository: TripRepository,
                                         carRepository: CarRepository): BookParkingSpotPresenter {
        return BookParkingSpotPresenterImpl(tripRepository, carRepository)
    }
}