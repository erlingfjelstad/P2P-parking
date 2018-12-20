package eu.vincinity2020.p2p_parking.ui.mylocations.addlocation

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface AddLocationPresenter : MvpPresenter {
    fun saveLocation(lat: String, lon : String, desc: String, email: String, password: String)
}