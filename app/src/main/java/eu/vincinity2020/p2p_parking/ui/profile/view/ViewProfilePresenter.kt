package eu.vincinity2020.p2p_parking.ui.profile.view

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface ViewProfilePresenter : MvpPresenter {
    fun getUserProfile(userId: Long)
}