package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface MyLocationListPresenter : MvpPresenter {
    fun getAllLocations(userId: Long)
}