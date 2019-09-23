package eu.vincinity2020.p2p_parking.ui.places.listplaces

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class PlacesListModule {

    @ActivityScope
    @Provides
    fun providesPlacesListPresenter(networkService: NetworkService): PlacesListPresenter {
        return PlacesListPresenter(networkService)
    }
}