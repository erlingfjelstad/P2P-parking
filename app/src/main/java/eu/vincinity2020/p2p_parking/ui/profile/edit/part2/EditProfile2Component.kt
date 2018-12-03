package eu.vincinity2020.p2p_parking.ui.profile.edit.part2

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [EditProfile2Module::class])
@FragmentScope
interface EditProfile2Component {
    fun inject(profileFragment: EditProfile2Fragment)

    @Subcomponent.Builder
    interface Builder {
        fun viewProfileModule(mapModule: EditProfile2Module): Builder

        fun build(): EditProfile2Component
    }
}