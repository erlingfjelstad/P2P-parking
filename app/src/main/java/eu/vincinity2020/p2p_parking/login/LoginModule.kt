package eu.vincinity2020.p2p_parking.login

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.NetworkService
import eu.vincinity2020.p2p_parking.common.ActivityScope

@Module
class LoginModule {

    @Provides
    @ActivityScope
    fun providesLoginPresenter(networkService: NetworkService): LoginPresenter {
        return LoginPresenterImpl(networkService)
    }
}
