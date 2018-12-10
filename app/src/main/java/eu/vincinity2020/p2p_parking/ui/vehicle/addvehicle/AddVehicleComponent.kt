package eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [AddVehicleModule::class])
@FragmentScope
interface AddVehicleComponent {
    fun inject(profileFragment: AddVehicleFragment)

    @Subcomponent.Builder
    interface Builder {
        fun addVehicleModule(mapModule: AddVehicleModule): Builder

        fun build(): AddVehicleComponent
    }
}