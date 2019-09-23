package eu.vincinity2020.p2p_parking.ui.places.listplaces

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [PlacesListModule::class])
@FragmentScope
interface PlacesListComponent {

    fun inject(placesListFragment: PlacesListFragment)

    @Subcomponent.Builder
    interface Builder{
        fun placesModules(placesListModule: PlacesListModule):Builder

        fun build(): PlacesListComponent
    }
}