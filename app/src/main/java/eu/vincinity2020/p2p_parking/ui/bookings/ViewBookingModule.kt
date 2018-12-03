package eu.vincinity2020.p2p_parking.ui.bookings

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class ViewBookingModule {

    @ActivityScope
    @Provides
    fun providesViewBookingsPresenter(networkService: NetworkService): ViewBookingsPresenter {
        return ViewBookingsPresenterImpl(networkService)
    }
}
