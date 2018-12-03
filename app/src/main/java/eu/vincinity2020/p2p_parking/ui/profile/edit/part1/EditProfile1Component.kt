package eu.vincinity2020.p2p_parking.ui.profile.edit.part1

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [EditProfile1Module::class])
@FragmentScope
interface EditProfile1Component {
    fun inject(profileFragment: EditProfile1Fragment)

    @Subcomponent.Builder
    interface Builder {
        fun viewProfileModule(mapModule: EditProfile1Module): Builder

        fun build(): EditProfile1Component
    }
}