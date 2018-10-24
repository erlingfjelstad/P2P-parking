package eu.vincinity2020.p2p_parking.login

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.common.ActivityScope

@Subcomponent(modules = [(LoginModule::class)])
@ActivityScope
interface LoginComponent {
    fun inject(loginActivity: LoginActivity)

    @Subcomponent.Builder
    interface Builder {
        fun loginModule(loginModule : LoginModule) : Builder

        fun build() : LoginComponent
    }
}