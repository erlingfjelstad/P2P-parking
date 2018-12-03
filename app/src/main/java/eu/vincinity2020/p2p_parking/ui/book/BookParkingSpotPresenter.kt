package eu.vincinity2020.p2p_parking.ui.book

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import java.util.*

interface BookParkingSpotPresenter : MvpPresenter {
    fun bookParkingSpot(fromDate: Date, toDate: Date, parkingSpotName: String)
}