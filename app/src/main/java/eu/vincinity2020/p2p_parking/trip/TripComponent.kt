package eu.vincinity2020.p2p_parking.trip

import dagger.Subcomponent

@Subcomponent(modules = [TripModule::class])
interface TripComponent {
    fun inject(recentTripsFragment: RecentTripsFragment)
    fun inject(upcomingTripsFragment: UpcomingTripsFragment)

    @Subcomponent.Builder
    interface Builder {
        fun tripModule(tripModule: TripModule): Builder
        fun build(): TripComponent
    }
}