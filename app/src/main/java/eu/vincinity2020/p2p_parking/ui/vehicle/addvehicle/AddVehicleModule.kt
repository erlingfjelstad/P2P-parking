package eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class AddVehicleModule {

    @ActivityScope
    @Provides
    fun providesViewBookingsPresenter(networkService: NetworkService): AddVehiclePresenter {
        return AddVehiclePresenterImpl(networkService)
    }
}
