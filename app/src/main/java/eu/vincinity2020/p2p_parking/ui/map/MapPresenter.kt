package eu.vincinity2020.p2p_parking.ui.map

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface MapPresenter : MvpPresenter {
    fun getNearbyParking(query: String)
}