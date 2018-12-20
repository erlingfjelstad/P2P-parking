package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.MyLocation


interface MyLocationListMvpView : MvpView {

    fun updateLocations(myLocations: ArrayList<MyLocation>)
    fun onLoadFinish()
}