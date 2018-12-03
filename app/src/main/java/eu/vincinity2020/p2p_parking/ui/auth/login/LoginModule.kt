package eu.vincinity2020.p2p_parking.ui.auth.login

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.app.common.ActivityScope

@Module
class LoginModule {

    @Provides
    @ActivityScope
    fun providesLoginPresenter(networkService: NetworkService): LoginPresenter {
        return LoginPresenterImpl(networkService)
    }
}
