package eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.VehicleTypes

interface AddVehicleMvpView : MvpView {

    fun updateVehicleTypes(vehicleTypes: ArrayList<VehicleTypes>)

    fun onVehicleAdded(message: String)

    fun onLoadFinish()
}