package eu.vincinity2020.p2p_parking.ui.search

import dagger.Subcomponent
import eu.vincinity2020.p2p_parking.app.common.ActivityScope

@ActivityScope
@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {
    fun inject(searchActivity: SearchActivity)

    @Subcomponent.Builder
    interface Builder {
        fun searchModule(searchModule: SearchModule): Builder

        fun build(): SearchComponent
    }
}