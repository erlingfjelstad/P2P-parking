package eu.vincinity2020.p2p_parking.ui.places.addplaces

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [AddPlacesModule::class])
@FragmentScope
interface AddPlacesComponent {

    fun inject(addPlacesFragment: AddPlacesFragment)

    @Subcomponent.Builder
    interface Builder{
        fun placesModule(addPlacesModule: AddPlacesModule): Builder

        fun build(): AddPlacesComponent
    }
}