package eu.vincinity2020.p2p_parking.car

import eu.vincinity2020.p2p_parking.common.Presenter
import eu.vincinity2020.p2p_parking.entities.Car

interface MyCarsPresenter : Presenter {
    fun registerCar(registrationNumber: String)
    fun setDefault(car: Car)
}