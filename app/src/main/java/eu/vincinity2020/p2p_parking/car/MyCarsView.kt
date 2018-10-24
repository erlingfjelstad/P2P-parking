package eu.vincinity2020.p2p_parking.car

import eu.vincinity2020.p2p_parking.common.View
import eu.vincinity2020.p2p_parking.entities.Car

interface MyCarsView : View {
    fun onCarAdded()
    fun onCarsLoaded(cars: List<Car>)
    fun onCarDefaulted()

}