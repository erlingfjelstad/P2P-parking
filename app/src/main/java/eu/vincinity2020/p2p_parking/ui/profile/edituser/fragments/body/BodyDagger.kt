package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.body

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserPresenter

@Subcomponent(modules = [BodyModule::class])
@FragmentScope
interface BodyComponent {
    fun inject(bodyFragment: BodyFragment)

    @Subcomponent.Builder
    interface Builder{
        fun bodyModule(bodyModule: BodyModule): Builder

        fun build(): BodyComponent
    }
}



@Module
@FragmentScope
class BodyModule {

    @ActivityScope
    @Provides
    fun providesBodyPresenter(networkService: NetworkService): EditUserPresenter {
        return EditUserPresenter(networkService)
    }
}
