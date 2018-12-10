package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.Vehicles

interface VehicleListMvpView : MvpView {
    fun updateVehicleList(vehicle: Vehicles)
    fun onLoadFinish()
}