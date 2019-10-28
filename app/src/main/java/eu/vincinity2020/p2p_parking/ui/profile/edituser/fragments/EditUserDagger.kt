package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserFragment
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserPresenter

@Subcomponent(modules = [EditUserModule::class])
@FragmentScope
interface EditUserComponent {
    fun inject(editUserFragment: EditUserFragment)

    @Subcomponent.Builder
    interface Builder{
        fun editUserModule(editUserModule: EditUserModule): Builder

        fun build(): EditUserComponent
    }
}



@Module
@FragmentScope
class EditUserModule {

    @ActivityScope
    @Provides
    fun providesEditUserPresenter(networkService: NetworkService): EditUserPresenter {
        return EditUserPresenter(networkService)
    }
}