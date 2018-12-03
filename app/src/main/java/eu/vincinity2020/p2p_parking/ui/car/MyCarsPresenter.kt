package eu.vincinity2020.p2p_parking.ui.car

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import eu.vincinity2020.p2p_parking.data.entities.Car

interface MyCarsPresenter : MvpPresenter {
    fun registerCar(registrationNumber: String)
    fun setDefault(car: Car)
}