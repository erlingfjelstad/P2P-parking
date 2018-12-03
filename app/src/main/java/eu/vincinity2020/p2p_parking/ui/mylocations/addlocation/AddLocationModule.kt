package eu.vincinity2020.p2p_parking.ui.mylocations.addlocation

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class AddLocationModule {

    @ActivityScope
    @Provides
    fun providesAddLocationPresenter(networkService: NetworkService): AddLocationPresenter {
        return AddLocationPresenterImpl(networkService)
    }
}
