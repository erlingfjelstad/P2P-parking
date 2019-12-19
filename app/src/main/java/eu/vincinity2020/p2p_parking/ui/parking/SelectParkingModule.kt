package eu.vincinity2020.p2p_parking.ui.parking

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class SelectParkingModule {

    @ActivityScope
    @Provides
    fun providesSelectParkingPresenter(networkService: NetworkService): SelectParkingPresenter {
        return SelectParkingPresenter(networkService)
    }
}