package eu.vincinity2020.p2p_parking.ui.map

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import eu.vincinity2020.p2p_parking.data.entities.ParkingSpot
import eu.vincinity2020.p2p_parking.data.entities.User

interface MapPresenter : MvpPresenter {
    fun getDefaultParkingSensors(email: String, password: String)
    fun getNearbyParkingSensors(email: String, password: String, lat: Double, lon: Double)
    fun createNewBooking(user: User, parkingSpot: ParkingSpot, fromTime: String, toTime: String)
}