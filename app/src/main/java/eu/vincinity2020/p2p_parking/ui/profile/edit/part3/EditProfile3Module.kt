package eu.vincinity2020.p2p_parking.ui.profile.edit.part3

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class EditProfile3Module {

    @ActivityScope
    @Provides
    fun providesProfilePresenter(networkService: NetworkService): EditProfile3Presenter {
        return EditProfile3PresenterImpl(networkService)
    }
}
