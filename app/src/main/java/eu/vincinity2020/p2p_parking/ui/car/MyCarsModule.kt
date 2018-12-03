package eu.vincinity2020.p2p_parking.ui.car

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.app.common.ActivityScope
import eu.vincinity2020.p2p_parking.data.repositories.CarRepository

@Module
class MyCarsModule {

    @Provides
    @ActivityScope
    fun providesMyCarPresenter(carRepository: CarRepository): MyCarsPresenter {
        return MyCarsPresenterImpl(carRepository)
    }

}
