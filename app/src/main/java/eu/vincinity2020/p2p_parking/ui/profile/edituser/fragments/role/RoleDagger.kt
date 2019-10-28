package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.role

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.app.common.FragmentScope
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserPresenter

@Subcomponent(modules = [RoleModule::class])
@FragmentScope
interface RoleComponent {
    fun inject(roleFragment: RoleFragment)

    @Subcomponent.Builder
    interface Builder{
        fun roleModule(roleModule: RoleModule): Builder

        fun build(): RoleComponent
    }
}



@Module
@FragmentScope
class RoleModule {

    @ActivityScope
    @Provides
    fun providesRolePresenter(networkService: NetworkService): EditUserPresenter {
        return EditUserPresenter(networkService)
    }
}
