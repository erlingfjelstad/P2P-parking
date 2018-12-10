package eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface AddVehiclePresenter : MvpPresenter {
    fun getAllBookingList(userId: Long)
}