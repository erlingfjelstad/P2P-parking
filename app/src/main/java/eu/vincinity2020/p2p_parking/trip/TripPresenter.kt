package eu.vincinity2020.p2p_parking.trip

import eu.vincinity2020.p2p_parking.common.Presenter

interface TripPresenter : Presenter {
    fun getRecentTrips()
}