package eu.vincinity2020.p2p_parking.trip

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.common.FragmentScope
import eu.vincinity2020.p2p_parking.repositories.TripRepository

@Module
class TripModule {

    @Provides
    @FragmentScope
    fun providesPresenter(tripRepository: TripRepository): TripPresenter {
        return TripPresenterImpl(tripRepository)
    }
}
