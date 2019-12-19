package eu.vincinity2020.p2p_parking.ui.parking

import com.google.maps.model.LatLng
import eu.vincinity2020.p2p_parking.data.entities.parking.ParkingSpot

class SelectParkingContract {

    interface View {
        fun showProgress()

        fun hideProgress()

        fun handleGetParkingSuccess(parkingSpots: ArrayList<ParkingSpot>)

        fun handleGetParkingFailure(e: Throwable)
    }

    interface Presenter{
        fun detach()

        fun getParkingSpots(view:View, location:LatLng)
    }
}