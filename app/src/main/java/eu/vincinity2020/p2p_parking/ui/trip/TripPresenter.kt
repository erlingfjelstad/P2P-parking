package eu.vincinity2020.p2p_parking.ui.trip

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface TripPresenter : MvpPresenter {
    fun getRecentTrips()
}