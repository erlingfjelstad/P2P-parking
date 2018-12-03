package eu.vincinity2020.p2p_parking.ui.car

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.Car

interface MyCarsView : MvpView {
    fun onCarAdded()
    fun onCarsLoaded(cars: List<Car>)
    fun onCarDefaulted()

}