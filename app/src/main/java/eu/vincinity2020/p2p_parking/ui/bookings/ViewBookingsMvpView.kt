package eu.vincinity2020.p2p_parking.ui.bookings

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.ParkingSpot
import eu.vincinity2020.p2p_parking.data.entities.User

interface ViewBookingsMvpView : MvpView {
    fun updateBookingList(user: User)
    fun onLoadFinish()
}