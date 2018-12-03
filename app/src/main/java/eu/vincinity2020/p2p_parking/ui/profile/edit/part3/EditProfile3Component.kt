package eu.vincinity2020.p2p_parking.ui.profile.edit.part3

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [EditProfile3Module::class])
@FragmentScope
interface EditProfile3Component {
    fun inject(profileFragment: EditProfile3Fragment)

    @Subcomponent.Builder
    interface Builder {
        fun viewProfileModule(mapModule: EditProfile3Module): Builder

        fun build(): EditProfile3Component
    }
}