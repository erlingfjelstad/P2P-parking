package eu.vincinity2020.p2p_parking.ui.bookings

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.FragmentScope

@Subcomponent(modules = [ViewBookingModule::class])
@FragmentScope
interface ViewBookingComponent {
    fun inject(profileFragment: ViewBookingsFragment)

    @Subcomponent.Builder
    interface Builder {
        fun viewBookingsModule(mapModule: ViewBookingModule): Builder

        fun build(): ViewBookingComponent
    }
}