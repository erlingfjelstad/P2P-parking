package eu.vincinity2020.p2p_parking.ui.mylocations.addlocation

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.User

interface AddLocationMvpView : MvpView {

    fun updateLocations(user: User)

    fun onLoadFinish()
}