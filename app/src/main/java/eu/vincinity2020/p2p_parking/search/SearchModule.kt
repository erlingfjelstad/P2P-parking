package eu.vincinity2020.p2p_parking.search

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.OpenStreetMapService
import eu.vincinity2020.p2p_parking.common.ActivityScope

@Module
class SearchModule {

    @ActivityScope
    @Provides
    fun providesSearchPresenter(openStreetMapService: OpenStreetMapService): SearchPresenter {
        return SearchPresenterImpl(openStreetMapService)
    }
}