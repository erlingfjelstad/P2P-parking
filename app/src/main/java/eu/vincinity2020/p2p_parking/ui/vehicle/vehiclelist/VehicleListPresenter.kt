package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface VehicleListPresenter : MvpPresenter {


    fun getVehicles(userId: Long, email:String, password: String)
}