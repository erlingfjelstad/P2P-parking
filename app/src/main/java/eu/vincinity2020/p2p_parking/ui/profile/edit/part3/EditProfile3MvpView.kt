package eu.vincinity2020.p2p_parking.ui.profile.edit.part3

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.User

interface EditProfile3MvpView : MvpView {
    fun updateUserProfileInfo(user: User)

    fun onLoadFinish()
}