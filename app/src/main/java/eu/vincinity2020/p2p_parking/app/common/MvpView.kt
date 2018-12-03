package eu.vincinity2020.p2p_parking.app.common

import eu.vincinity2020.p2p_parking.data.entities.ParkingSpot

interface MvpView {
    fun onUnknownError(errorMessage: String)
    fun onTimeout()
    fun onNetworkError()
}