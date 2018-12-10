package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [VehicleListModule::class])
@FragmentScope
interface VehicleListComponent {
    fun inject(profileFragment: VehicleListFragment)

    @Subcomponent.Builder
    interface Builder {
        fun viewVehiclesModule(mapModule: VehicleListModule): Builder

        fun build(): VehicleListComponent
    }
}