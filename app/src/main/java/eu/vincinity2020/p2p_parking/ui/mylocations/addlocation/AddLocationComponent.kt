package eu.vincinity2020.p2p_parking.ui.mylocations.addlocation

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [AddLocationModule::class])
@FragmentScope
interface AddLocationComponent {
    fun inject(profileFragment: AddLocationFragment)

    @Subcomponent.Builder
    interface Builder {
        fun addLocationModule(mapModule: AddLocationModule): Builder

        fun build(): AddLocationComponent
    }
}