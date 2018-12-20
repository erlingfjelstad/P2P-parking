package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import eu.vincinity2020.p2p_parking.data.entities.User

interface MyLocationListPresenter : MvpPresenter {
    fun getAllLocations(user: User)
}