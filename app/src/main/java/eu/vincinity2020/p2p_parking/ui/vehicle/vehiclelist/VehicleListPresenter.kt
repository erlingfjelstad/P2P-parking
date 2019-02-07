package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import eu.vincinity2020.p2p_parking.data.entities.Vehicles

interface VehicleListPresenter : MvpPresenter {


    fun getVehicles(userId: Long, email:String, password: String)

    fun updateDefaultVehicle(userId: Long, email:String, password: String, selectedVehicle: Vehicles, position: Int)
}