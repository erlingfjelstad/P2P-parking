package eu.vincinity2020.p2p_parking.ui.profile.edit.part3

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface EditProfile3Presenter : MvpPresenter {
    fun getUserProfile(userId: Long)
}