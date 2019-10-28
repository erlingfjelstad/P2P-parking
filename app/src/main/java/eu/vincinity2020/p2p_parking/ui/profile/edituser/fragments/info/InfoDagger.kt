package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.info

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserPresenter

@Subcomponent(modules = [InfoModule::class])
@FragmentScope
interface InfoComponent {
    fun inject(infoFragment: InfoFragment)

    @Subcomponent.Builder
    interface Builder{
        fun infoModule(infoModule: InfoModule): Builder

        fun build(): InfoComponent
    }
}



@Module
@FragmentScope
class InfoModule {

    @ActivityScope
    @Provides
    fun providesInfoPresenter(networkService: NetworkService): EditUserPresenter {
        return EditUserPresenter(networkService)
    }
}
