package eu.vincinity2020.p2p_parking.book

import eu.vincinity2020.p2p_parking.common.Presenter
import java.util.*

interface BookParkingSpotPresenter : Presenter {
    fun bookParkingSpot(fromDate: Date, toDate: Date, parkingSpotName: String)
}