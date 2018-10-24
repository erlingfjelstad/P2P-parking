package eu.vincinity2020.p2p_parking.map

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.common.FragmentScope

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