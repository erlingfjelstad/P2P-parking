package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
class RegisterUserModule {

    @Provides
    @ActivityScope
    fun providesRegistrationPresenter(networkService: NetworkService): RegistrationPresenter {
        return RegisterPresenterImpl(networkService)
    }
}
