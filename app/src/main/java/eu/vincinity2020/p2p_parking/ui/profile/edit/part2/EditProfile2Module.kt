package eu.vincinity2020.p2p_parking.ui.profile.edit.part2

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService

@Module
@FragmentScope
class EditProfile2Module {

    @ActivityScope
    @Provides
    fun providesProfilePresenter(networkService: NetworkService): EditProfile2Presenter {
        return EditProfile2PresenterImpl(networkService)
    }
}
