package eu.vincinity2020.p2p_parking.registeruser

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.common.ActivityScope

@Subcomponent(modules = [RegisterUserModule::class])
@ActivityScope
interface RegisterUserComponent {
    fun inject(registerUserActivity: RegisterUserActivity)

    @Subcomponent.Builder
    interface Builder {
        fun registerUserModule(registerUserModule: RegisterUserModule): Builder
        fun build(): RegisterUserComponent
    }
}