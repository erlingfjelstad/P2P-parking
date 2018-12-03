package eu.vincinity2020.p2p_parking.ui.trip

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.data.repositories.TripRepository

@Module
class TripModule {

    @Provides
    @FragmentScope
    fun providesPresenter(tripRepository: TripRepository): TripPresenter {
        return TripPresenterImpl(tripRepository)
    }
}
