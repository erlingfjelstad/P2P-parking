package eu.vincinity2020.p2p_parking.ui.map

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class MapModule {

    @ActivityScope
    @Provides
    fun providesMapPresenter(networkService: NetworkService): MapPresenter {
        return MapPresenterImpl(networkService)
    }
}
