package eu.vincinity2020.p2p_parking.ui.map

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [MapModule::class])
@FragmentScope
interface MapComponent {
    fun inject(mapFragment: MapFragment)

    @Subcomponent.Builder
    interface Builder {
        fun mapModule(mapModule: MapModule): Builder

        fun build(): MapComponent
    }
}