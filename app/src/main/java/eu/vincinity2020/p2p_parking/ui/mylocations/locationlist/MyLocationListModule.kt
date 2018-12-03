package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class MyLocationListModule {

    @ActivityScope
    @Provides
    fun providesLocationListPresenter(networkService: NetworkService): MyLocationListPresenter {
        return MyLocationListPresenterImpl(networkService)
    }
}
