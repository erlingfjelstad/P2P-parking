package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.ParkingSpot
import eu.vincinity2020.p2p_parking.data.entities.User

interface MyLocationListMvpView : MvpView {

    fun updateLocations(user: User)
    fun onLoadFinish()
}