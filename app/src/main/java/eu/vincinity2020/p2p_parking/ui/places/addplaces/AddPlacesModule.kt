package eu.vincinity2020.p2p_parking.ui.places.addplaces

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class AddPlacesModule {

    @ActivityScope
    @Provides
    fun providesLocationListPresenter(networkService: NetworkService): AddPlacesPresenter {
        return AddPlacesPresenter(networkService)
    }
}