package eu.vincinity2020.p2p_parking.ui.profile.view

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.User

interface ViewProfileMvpView : MvpView {
    fun updateUserProfileInfo(user: User)
    fun onLoadFinish()
}