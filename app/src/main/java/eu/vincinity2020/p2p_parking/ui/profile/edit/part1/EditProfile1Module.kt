package eu.vincinity2020.p2p_parking.ui.profile.edit.part1

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class EditProfile1Module {

    @ActivityScope
    @Provides
    fun providesProfilePresenter(networkService: NetworkService): EditProfile1Presenter {
        return EditProfile1PresenterImpl(networkService)
    }
}
