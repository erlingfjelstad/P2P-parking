package eu.vincinity2020.p2p_parking.book

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.common.ActivityScope

@Subcomponent(modules = [BookParkingSpotModule::class])
@ActivityScope
interface BookParkingSpotComponent {
    fun inject(bookParkingSpotActivity: BookParkingSpotActivity)

    @Subcomponent.Builder
    interface Builder {
        fun bookParkingSpotModule(bookParkingSpotModule: BookParkingSpotModule): Builder

        fun build(): BookParkingSpotComponent
    }
}