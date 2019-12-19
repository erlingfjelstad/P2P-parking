package eu.vincinity2020.p2p_parking.ui.parking

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [SelectParkingModule::class])
@FragmentScope
interface SelectParkingComponet {

    fun inject(selectParkingSpotsFragment: SelectParkingSpotsFragment)

    @Subcomponent.Builder
    interface Builder {
        fun selectParkingModule(selectParkingModule: SelectParkingModule): Builder

        fun build(): SelectParkingComponet
    }
}