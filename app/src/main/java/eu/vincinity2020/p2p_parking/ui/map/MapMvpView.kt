package eu.vincinity2020.p2p_parking.ui.map

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.ParkingSensor

interface MapMvpView : MvpView {
    fun getMarkers(marker:ArrayList<ParkingSensor>)
    fun onLoadFinish()
}