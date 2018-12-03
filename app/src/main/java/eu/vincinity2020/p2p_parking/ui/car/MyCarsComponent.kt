package eu.vincinity2020.p2p_parking.ui.car

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.ActivityScope

@Subcomponent(modules = [MyCarsModule::class])
@ActivityScope
interface MyCarsComponent {
    fun inject(myCarsActivity: MyCarsActivity)

    @Subcomponent.Builder
    interface Builder {
        fun myCarsModule(myCarsModule: MyCarsModule): Builder

        fun build(): MyCarsComponent
    }
}