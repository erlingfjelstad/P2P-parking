package eu.vincinity2020.p2p_parking.ui.bookings

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import eu.vincinity2020.p2p_parking.data.entities.User

interface ViewBookingsPresenter : MvpPresenter {
    fun getAllBookingList(user: User)

}