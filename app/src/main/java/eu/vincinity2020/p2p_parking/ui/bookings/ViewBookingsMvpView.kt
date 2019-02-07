package eu.vincinity2020.p2p_parking.ui.bookings

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.Bookings

interface ViewBookingsMvpView : MvpView {
    fun updateBookingList(bookings: ArrayList<Bookings>)
    fun onLoadFinish()
}