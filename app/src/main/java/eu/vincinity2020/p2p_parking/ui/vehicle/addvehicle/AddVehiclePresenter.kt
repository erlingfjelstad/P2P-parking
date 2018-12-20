package eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import eu.vincinity2020.p2p_parking.data.entities.VehicleTypes

interface AddVehiclePresenter : MvpPresenter {

    fun registerNewVehicle(email:String, password: String, userId: Long, brandName: String, model: String, description: String,
                           regNo: String, mfgYear: String,
                           vehicleType: VehicleTypes, height: String, width: String, length: String, weight: String, fuelType: String)

    fun getVehicleTypes(userId: Long)
}