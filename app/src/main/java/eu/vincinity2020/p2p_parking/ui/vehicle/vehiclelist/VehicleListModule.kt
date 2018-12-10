package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class VehicleListModule {

    @ActivityScope
    @Provides
    fun providesViewBookingsPresenter(networkService: NetworkService): VehicleListPresenter {
        return VehicleListPresenterImpl(networkService)
    }
}
