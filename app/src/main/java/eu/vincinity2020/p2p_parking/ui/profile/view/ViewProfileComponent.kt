package eu.vincinity2020.p2p_parking.ui.profile.view

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [ViewProfileModule::class])
@FragmentScope
interface ViewProfileComponent {
    fun inject(profileFragment: ViewProfileFragment)

    @Subcomponent.Builder
    interface Builder {
        fun viewProfileModule(mapModule: ViewProfileModule): Builder

        fun build(): ViewProfileComponent
    }
}