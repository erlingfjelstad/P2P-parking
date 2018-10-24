package eu.vincinity2020.p2p_parking.car

import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.common.ActivityScope
import eu.vincinity2020.p2p_parking.repositories.CarRepository

@Module
class MyCarsModule {

    @Provides
    @ActivityScope
    fun providesMyCarPresenter(carRepository: CarRepository): MyCarsPresenter {
        return MyCarsPresenterImpl(carRepository)
    }

}
