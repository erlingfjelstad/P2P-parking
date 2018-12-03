package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [MyLocationListModule::class])
@FragmentScope
interface MyLocationListComponent {
    fun inject(profileFragment: MyLocationListFragment)

    @Subcomponent.Builder
    interface Builder {
        fun myLocationListModule(mapModule: MyLocationListModule): Builder

        fun build(): MyLocationListComponent
    }
}