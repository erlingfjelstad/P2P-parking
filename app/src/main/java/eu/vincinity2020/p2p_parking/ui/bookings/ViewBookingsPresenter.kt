package eu.vincinity2020.p2p_parking.ui.bookings

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface ViewBookingsPresenter : MvpPresenter {
    fun getAllBookingList(userId: Long)

}