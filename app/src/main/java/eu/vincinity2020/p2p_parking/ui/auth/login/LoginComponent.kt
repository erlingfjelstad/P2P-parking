package eu.vincinity2020.p2p_parking.ui.auth.login

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.ActivityScope

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