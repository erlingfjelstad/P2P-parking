package eu.vincinity2020.p2p_parking.ui.profile.edit.part1

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface EditProfile1Presenter : MvpPresenter {
    fun getUserProfile(userId: Long)
}