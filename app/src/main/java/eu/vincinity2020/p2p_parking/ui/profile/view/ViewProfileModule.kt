package eu.vincinity2020.p2p_parking.ui.profile.view

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class ViewProfileModule {

    @ActivityScope
    @Provides
    fun providesViewProfilePresenter(networkService: NetworkService): ViewProfilePresenter {
        return ViewProfilePresenterImpl(networkService)
    }
}
